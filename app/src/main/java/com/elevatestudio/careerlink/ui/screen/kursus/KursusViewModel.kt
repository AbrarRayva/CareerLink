// Lokasi: ui/screen/kursus/KursusViewModel.kt
package com.elevatestudio.careerlink.ui.screen.kursus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevatestudio.careerlink.ui.screen.lowongan.SubmissionState // Kita pakai ulang state dari Lowongan
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KursusViewModel : ViewModel() {

    // State untuk nanganin pop-up (Loading, Success, Error)
    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionState: StateFlow<SubmissionState> = _submissionState

    /**
     * Dipanggil pas klik "DAFTAR SEKARANG"
     */
    fun daftarKursus(kursusId: String) {
        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            try {
                // Simulasi panggil API
                delay(1500)
                _submissionState.value = SubmissionState.Success
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Dipanggil pas upload file sertifikat
     */
    fun uploadSertifikat(file: Any) { // Nanti 'Any' diganti jadi MultipartBody.Part
        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            try {
                delay(1500)
                _submissionState.value = SubmissionState.Success
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error(e.message ?: "Gagal mengunggah file")
            }
        }
    }

    /**
     * Dipanggil pas selesai scan QR
     */
    fun scanSertifikat(qrData: String) {
        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            try {
                delay(1000)
                _submissionState.value = SubmissionState.Success
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error(e.message ?: "QR Code tidak valid")
            }
        }
    }

    /**
     * Dipanggil setelah dialog/snackbar ditutup
     */
    fun resetSubmissionState() {
        _submissionState.value = SubmissionState.Idle
    }
}