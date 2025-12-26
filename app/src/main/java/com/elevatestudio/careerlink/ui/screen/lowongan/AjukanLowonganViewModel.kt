package com.elevatestudio.careerlink.ui.screen.lowongan

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevatestudio.careerlink.data.remote.RetrofitClient
import com.elevatestudio.careerlink.utils.FileUtils
import com.elevatestudio.careerlink.utils.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

sealed interface SubmissionState {
    object Idle : SubmissionState
    object Loading : SubmissionState
    object Success : SubmissionState
    data class Error(val message: String) : SubmissionState
}

data class AjukanLamaranState(
    val namaLengkap: String = "",
    val tanggalLahir: String = "",
    val jenisKelamin: String = "",
    val pendidikan: String = "",
    val programStudi: String = "",
    val nomorAktif: String = "+62",
    val ceritakanDirimu: String = "",
    val cvUri: String? = null,
    val portofolioUri: String? = null,
    val suratRekomendasiUri: String? = null,

    val tanggalLahirError: String? = null,
    val nomorAktifError: String? = null
) {
    val wordCount: Int
        get() = if (ceritakanDirimu.isBlank()) 0 else ceritakanDirimu.trim().split(Regex("\\s+")).size

    val isFormValid: Boolean
        get() = namaLengkap.isNotBlank() &&
                tanggalLahir.isNotBlank() && tanggalLahirError == null &&
                jenisKelamin.isNotBlank() &&
                pendidikan.isNotBlank() &&
                programStudi.isNotBlank() &&
                nomorAktif.isNotBlank() && nomorAktifError == null &&
                wordCount >= 80 &&
                cvUri != null &&
                suratRekomendasiUri != null
}

sealed interface LamaranFormEvent {
    data class NamaChanged(val value: String) : LamaranFormEvent
    data class TanggalLahirChanged(val value: String) : LamaranFormEvent
    data class JenisKelaminChanged(val value: String) : LamaranFormEvent
    data class PendidikanChanged(val value: String) : LamaranFormEvent
    data class ProgramStudiChanged(val value: String) : LamaranFormEvent
    data class NomorAktifChanged(val value: String) : LamaranFormEvent
    data class CeritakanDirimuChanged(val value: String) : LamaranFormEvent
    data class CvUploaded(val uri: String?) : LamaranFormEvent
    data class PortofolioUploaded(val uri: String?) : LamaranFormEvent
    data class SuratRekomendasiUploaded(val uri: String?) : LamaranFormEvent
    object ClearCv : LamaranFormEvent
    object ClearPortofolio : LamaranFormEvent
    object ClearSuratRekomendasi : LamaranFormEvent
    object Submit : LamaranFormEvent
}

class AjukanLowonganViewModel : ViewModel() {

    private val _lamaranState = MutableStateFlow(AjukanLamaranState())
    val lamaranState: StateFlow<AjukanLamaranState> = _lamaranState.asStateFlow()

    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionState: StateFlow<SubmissionState> = _submissionState.asStateFlow()

    private val dateRegex = Regex("""^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/\d{4}$""")

   
    private val phoneRegex = Regex("""^(\+62)8[1-9][0-9]{6,11}$""")

    fun onLamaranEvent(event: LamaranFormEvent) {
        when (event) {
            is LamaranFormEvent.NamaChanged -> _lamaranState.update { it.copy(namaLengkap = event.value) }

            is LamaranFormEvent.TanggalLahirChanged -> {
                val input = event.value
                val error = if (input.isNotEmpty() && !input.matches(dateRegex)) {
                    "Wajib format DD/MM/YYYY (Contoh: 20/03/2005)"
                } else null

                _lamaranState.update { it.copy(tanggalLahir = input, tanggalLahirError = error) }
            }

            is LamaranFormEvent.JenisKelaminChanged -> _lamaranState.update { it.copy(jenisKelamin = event.value) }
            is LamaranFormEvent.PendidikanChanged -> _lamaranState.update { it.copy(pendidikan = event.value) }
            is LamaranFormEvent.ProgramStudiChanged -> _lamaranState.update { it.copy(programStudi = event.value) }
            is LamaranFormEvent.NomorAktifChanged -> {
                val input = event.value
                val error = if (input.isNotEmpty() && !input.matches(phoneRegex)) {
                    "Wajib diawali +628... (Contoh: +62812345678)"
                } else null

                _lamaranState.update { it.copy(nomorAktif = input, nomorAktifError = error) }
            }

            is LamaranFormEvent.CeritakanDirimuChanged -> _lamaranState.update { it.copy(ceritakanDirimu = event.value) }
            is LamaranFormEvent.CvUploaded -> _lamaranState.update { it.copy(cvUri = event.uri) }
            is LamaranFormEvent.PortofolioUploaded -> _lamaranState.update { it.copy(portofolioUri = event.uri) }
            is LamaranFormEvent.SuratRekomendasiUploaded -> _lamaranState.update { it.copy(suratRekomendasiUri = event.uri) }

            LamaranFormEvent.ClearCv -> _lamaranState.update { it.copy(cvUri = null) }
            LamaranFormEvent.ClearPortofolio -> _lamaranState.update { it.copy(portofolioUri = null) }
            LamaranFormEvent.ClearSuratRekomendasi -> _lamaranState.update { it.copy(suratRekomendasiUri = null) }

            LamaranFormEvent.Submit -> {}
        }
    }

    fun submitLamaran(context: Context, lowonganId: String) {
        val currentState = _lamaranState.value
        if (!currentState.isFormValid) {
            _submissionState.value = SubmissionState.Error("Mohon perbaiki data yang bertanda merah!")
            return
        }

        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            try {
                val userPreferences = UserPreferences(context)
                val tokenString = userPreferences.authToken.first()
                if (tokenString.isNullOrEmpty()) {
                    _submissionState.value = SubmissionState.Error("Anda belum login.")
                    return@launch
                }
                val token = "Bearer $tokenString"

                val cvUri = Uri.parse(currentState.cvUri)
                val cvFile = FileUtils.getFileFromUri(context, cvUri)
                val recUri = Uri.parse(currentState.suratRekomendasiUri!!)
                val recFile = FileUtils.getFileFromUri(context, recUri)

                if (cvFile != null && recFile != null) {
                    val cvRequest = cvFile.asRequestBody("application/pdf".toMediaTypeOrNull())
                    val cvPart = MultipartBody.Part.createFormData("cv", cvFile.name, cvRequest)
                    val recRequest = recFile.asRequestBody("application/pdf".toMediaTypeOrNull())
                    val recPart = MultipartBody.Part.createFormData("recommendation_letter", recFile.name, recRequest)

                    var portfolioPart: MultipartBody.Part? = null
                    if (currentState.portofolioUri != null) {
                        val portUri = Uri.parse(currentState.portofolioUri)
                        val portFile = FileUtils.getFileFromUri(context, portUri)
                        if (portFile != null) {
                            val portRequest = portFile.asRequestBody("application/pdf".toMediaTypeOrNull())
                            portfolioPart = MultipartBody.Part.createFormData("portfolio", portFile.name, portRequest)
                        }
                    }

                    fun createPart(value: String): okhttp3.RequestBody {
                        return value.toRequestBody("text/plain".toMediaTypeOrNull())
                    }
                    val rawDate = currentState.tanggalLahir
                    val dateParts = rawDate.split("/")
                    val apiDate = if (dateParts.size == 3) {
                        "${dateParts[2]}-${dateParts[1]}-${dateParts[0]}"
                    } else rawDate

                    val response = RetrofitClient.instance.ajukanLowongan(
                        token = token,
                        lowonganId = lowonganId,
                        cv = cvPart,
                        recommendation_letter = recPart,
                        portfolio = portfolioPart,
                        fullName = createPart(currentState.namaLengkap),
                        dob = createPart(apiDate),
                        gender = createPart(currentState.jenisKelamin),
                        education = createPart(currentState.pendidikan),
                        major = createPart(currentState.programStudi),
                        phone = createPart(currentState.nomorAktif),
                        aboutMe = createPart(currentState.ceritakanDirimu)
                    )

                    if (response.isSuccessful) {
                        _submissionState.value = SubmissionState.Success
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: response.message()
                        _submissionState.value = SubmissionState.Error("Gagal: $errorMsg")
                    }
                } else {
                    _submissionState.value = SubmissionState.Error("Gagal membaca file.")
                }
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error("Error: ${e.message}")
            }
        }
    }

    fun resetSubmissionState() {
        _submissionState.value = SubmissionState.Idle
    }
}