package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevatestudio.careerlink.data.model.LowonganItem
import com.elevatestudio.careerlink.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// State untuk UI: Loading, Sukses (bawa data), atau Error
sealed interface LowonganUiState {
    object Loading : LowonganUiState
    data class Success(val lowongan: List<LowonganItem>) : LowonganUiState
    data class Error(val message: String) : LowonganUiState
}

class DaftarLowonganViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<LowonganUiState>(LowonganUiState.Loading)
    val uiState: StateFlow<LowonganUiState> = _uiState.asStateFlow()

    init {
        // Otomatis ambil data saat ViewModel dibuat
        getAllLowongan()
    }

    fun getAllLowongan(searchQuery: String? = null) {
        viewModelScope.launch {
            _uiState.value = LowonganUiState.Loading
            try {
                // Panggil API getLowongan yang sudah kita perbaiki tadi
                val response = RetrofitClient.instance.getLowongan(query = searchQuery)

                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = LowonganUiState.Success(response.body()!!)
                } else {
                    _uiState.value = LowonganUiState.Error("Gagal memuat data: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = LowonganUiState.Error("Error koneksi: ${e.message}")
            }
        }
    }
}