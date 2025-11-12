// Lokasi: ui/screen/lowongan/LowonganViewModel.kt
package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
                suratRekomendasiUri != null
}

/**
 * Ini adalah "perintah" yang dikirim dari UI (Screen) ke ViewModel.
 */
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

    // --- TAMBAHAN BARU UNTUK HAPUS FILE ---
    object ClearCv : LamaranFormEvent
    object ClearPortofolio : LamaranFormEvent
    object ClearSuratRekomendasi : LamaranFormEvent
    // --- SELESAI TAMBAHAN ---

    object Submit : LamaranFormEvent
}


class LowonganViewModel : ViewModel() {

    private val _lamaranState = MutableStateFlow(AjukanLamaranState())
    val lamaranState: StateFlow<AjukanLamaranState> = _lamaranState.asStateFlow()

    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionState: StateFlow<SubmissionState> = _submissionState.asStateFlow()

    fun onLamaranEvent(event: LamaranFormEvent) {
        when (event) {
            // Update state sesuai 'event' yang masuk
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

            // --- TAMBAHAN BARU UNTUK HAPUS FILE ---
            LamaranFormEvent.ClearCv -> _lamaranState.update { it.copy(cvUri = null) }
            LamaranFormEvent.ClearPortofolio -> _lamaranState.update { it.copy(portofolioUri = null) }
            LamaranFormEvent.ClearSuratRekomendasi -> _lamaranState.update { it.copy(suratRekomendasiUri = null) }
            // --- SELESAI TAMBAHAN ---

            LamaranFormEvent.Submit -> {
                submitLamaran()
            }
        }
    }

    private fun submitLamaran() {
        if (!_lamaranState.value.isFormValid) return

        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            try {
                delay(2000)
                val isSuccess = true
                if (isSuccess) {
                    _submissionState.value = SubmissionState.Success
                } else {
                    throw Exception("Terjadi kesalahan, silahkan coba lagi")
                }
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetSubmissionState() {
        _submissionState.value = SubmissionState.Idle
    }
}