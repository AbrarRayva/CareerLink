package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// 🔥 DUA IMPORT INI WAJIB ADA 🔥
import com.elevatestudio.careerlink.data.model.LowonganDetail
import com.elevatestudio.careerlink.data.model.LowonganItem
import com.elevatestudio.careerlink.data.remote.ApiClient
import kotlinx.coroutines.launch

sealed class LowonganUiState {
    object Loading : LowonganUiState()
    data class Success(val data: List<LowonganItem>) : LowonganUiState()
    data class Error(val message: String) : LowonganUiState()
}

sealed class DetailLowonganUiState {
    object Loading : DetailLowonganUiState()
    data class Success(val data: LowonganDetail) : DetailLowonganUiState()
    data class Error(val message: String) : DetailLowonganUiState()
}

class DaftarLowonganViewModel : ViewModel() {
    var lowonganState: LowonganUiState by mutableStateOf(LowonganUiState.Loading)
        private set

    var detailState: DetailLowonganUiState by mutableStateOf(DetailLowonganUiState.Loading)
        private set

    private val apiService = ApiClient.instance

    fun getLowongan(search: String? = null, type: String? = null) {
        viewModelScope.launch {
            lowonganState = LowonganUiState.Loading
            try {
                val response = apiService.getLowongan(search, type, token = null)
                if (response.isSuccessful) {
                    lowonganState = LowonganUiState.Success(response.body() ?: emptyList())
                } else {
                    lowonganState = LowonganUiState.Error("Gagal memuat data")
                }
            } catch (e: Exception) {
                lowonganState = LowonganUiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun getDetailLowongan(id: String) {
        viewModelScope.launch {
            detailState = DetailLowonganUiState.Loading
            try {
                val response = apiService.getDetailLowongan(token = null, lowonganId = id)
                if (response.isSuccessful && response.body() != null) {
                    detailState = DetailLowonganUiState.Success(response.body()!!)
                } else {
                    detailState = DetailLowonganUiState.Error("Gagal memuat detail")
                }
            } catch (e: Exception) {
                detailState = DetailLowonganUiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}