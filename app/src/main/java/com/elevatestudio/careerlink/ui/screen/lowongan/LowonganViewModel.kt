// Lokasi: ui/screen/lowongan/LowonganViewModel.kt
package com.elevatestudio.careerlink.ui.screen.lowongan

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevatestudio.careerlink.data.remote.RetrofitClient
import com.elevatestudio.careerlink.utils.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    val nomorAktif: String = "",
    val ceritakanDirimu: String = "",
    val cvUri: String? = null,
    val portofolioUri: String? = null,
    val suratRekomendasiUri: String? = null,
) {
    val wordCount: Int
        get() = ceritakanDirimu.split(Regex("\\s+")).filter { it.isNotBlank() }.size

    val isFormValid: Boolean
        get() = namaLengkap.isNotBlank() &&
                tanggalLahir.isNotBlank() &&
                jenisKelamin.isNotBlank() &&
                pendidikan.isNotBlank() &&
                programStudi.isNotBlank() &&
                nomorAktif.isNotBlank() &&
                wordCount >= 80 &&
                cvUri != null &&
                suratRekomendasiUri != null // <-- Validasi Surat Rekomendasi Wajib
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

class LowonganViewModel : ViewModel() {

    private val _lamaranState = MutableStateFlow(AjukanLamaranState())
    val lamaranState: StateFlow<AjukanLamaranState> = _lamaranState.asStateFlow()

    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionState: StateFlow<SubmissionState> = _submissionState.asStateFlow()

    fun onLamaranEvent(event: LamaranFormEvent) {
        when (event) {
            is LamaranFormEvent.NamaChanged -> _lamaranState.update { it.copy(namaLengkap = event.value) }
            is LamaranFormEvent.TanggalLahirChanged -> _lamaranState.update { it.copy(tanggalLahir = event.value) }
            is LamaranFormEvent.JenisKelaminChanged -> _lamaranState.update { it.copy(jenisKelamin = event.value) }
            is LamaranFormEvent.PendidikanChanged -> _lamaranState.update { it.copy(pendidikan = event.value) }
            is LamaranFormEvent.ProgramStudiChanged -> _lamaranState.update { it.copy(programStudi = event.value) }
            is LamaranFormEvent.NomorAktifChanged -> _lamaranState.update { it.copy(nomorAktif = event.value) }
            is LamaranFormEvent.CeritakanDirimuChanged -> _lamaranState.update { it.copy(ceritakanDirimu = event.value) }
            is LamaranFormEvent.CvUploaded -> _lamaranState.update { it.copy(cvUri = event.uri) }
            is LamaranFormEvent.PortofolioUploaded -> _lamaranState.update { it.copy(portofolioUri = event.uri) }
            is LamaranFormEvent.SuratRekomendasiUploaded -> _lamaranState.update { it.copy(suratRekomendasiUri = event.uri) }

            LamaranFormEvent.ClearCv -> _lamaranState.update { it.copy(cvUri = null) }
            LamaranFormEvent.ClearPortofolio -> _lamaranState.update { it.copy(portofolioUri = null) }
            LamaranFormEvent.ClearSuratRekomendasi -> _lamaranState.update { it.copy(suratRekomendasiUri = null) }

            LamaranFormEvent.Submit -> { /* No-op, dipanggil langsung dari UI */ }
        }
    }

    fun submitLamaran(context: Context, lowonganId: String) {
        val currentState = _lamaranState.value
        if (!currentState.isFormValid) return

        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            try {
                // 1. SIAPKAN FILE CV (Wajib)
                val cvUri = Uri.parse(currentState.cvUri)
                val cvFile = FileUtils.getFileFromUri(context, cvUri)

                // 2. SIAPKAN SURAT REKOMENDASI (Wajib)
                val recUri = Uri.parse(currentState.suratRekomendasiUri!!) // Aman di-force unwrap karena isFormValid
                val recFile = FileUtils.getFileFromUri(context, recUri)

                if (cvFile != null && recFile != null) {

                    // -- Proses CV --
                    val cvRequest = cvFile.asRequestBody("application/pdf".toMediaTypeOrNull())
                    val cvPart = MultipartBody.Part.createFormData("cv", cvFile.name, cvRequest)

                    // -- Proses Surat Rekomendasi --
                    val recRequest = recFile.asRequestBody("application/pdf".toMediaTypeOrNull())
                    val recPart = MultipartBody.Part.createFormData("recommendation_letter", recFile.name, recRequest)

                    // -- Proses Portofolio (Opsional) --
                    var portfolioPart: MultipartBody.Part? = null
                    if (currentState.portofolioUri != null) {
                        val portUri = Uri.parse(currentState.portofolioUri)
                        val portFile = FileUtils.getFileFromUri(context, portUri)
                        if (portFile != null) {
                            val portRequest = portFile.asRequestBody("application/pdf".toMediaTypeOrNull())
                            portfolioPart = MultipartBody.Part.createFormData("portfolio", portFile.name, portRequest)
                        }
                    }

                    // 3. SIAPKAN DATA TEKS
                    fun createPart(value: String): RequestBody {
                        return value.toRequestBody("text/plain".toMediaTypeOrNull())
                    }

                    val fullNamePart = createPart(currentState.namaLengkap)
                    val dobPart = createPart(currentState.tanggalLahir)
                    val genderPart = createPart(currentState.jenisKelamin)
                    val educationPart = createPart(currentState.pendidikan)
                    val majorPart = createPart(currentState.programStudi)
                    val phonePart = createPart(currentState.nomorAktif)
                    val aboutPart = createPart(currentState.ceritakanDirimu)

                    // 4. TOKEN (Hardcoded sementara)
                    val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwicm9sZSI6InN0dWRlbnQiLCJpYXQiOjE3NjQ5MzI0OTAsImV4cCI6MTc2NTUzNzI5MH0.0hJfzE1uaKkY3Fdz6Uo_tjBMZO-f2HQQftXJB6gBWSI"

                    // 5. TEMBAK API
                    val response = RetrofitClient.instance.ajukanLowongan(
                        token = token,
                        lowonganId = lowonganId,
                        cv = cvPart,
                        recommendation_letter = recPart, // Masuk sebagai parameter wajib
                        portfolio = portfolioPart,       // Masuk sebagai parameter opsional (Multipart)
                        fullName = fullNamePart,
                        dob = dobPart,
                        gender = genderPart,
                        education = educationPart,
                        major = majorPart,
                        phone = phonePart,
                        aboutMe = aboutPart
                    )

                    if (response.isSuccessful) {
                        _submissionState.value = SubmissionState.Success
                    } else {
                        _submissionState.value = SubmissionState.Error("Gagal: ${response.message()}")
                    }
                } else {
                    _submissionState.value = SubmissionState.Error("Gagal membaca file dokumen")
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