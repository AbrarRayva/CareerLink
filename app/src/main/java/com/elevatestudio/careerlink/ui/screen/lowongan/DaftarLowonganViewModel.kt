// Lokasi: ui/screen/lowongan/DaftarLowonganViewModel.kt
package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevatestudio.careerlink.data.model.LowonganDetail
import com.elevatestudio.careerlink.data.model.LowonganItem
import com.elevatestudio.careerlink.data.remote.RetrofitClient
import com.elevatestudio.careerlink.utils.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// State untuk UI List Lowongan
sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val jobs: List<LowonganItem>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

// State untuk Detail Lowongan
sealed interface DetailUiState {
    object Idle : DetailUiState
    object Loading : DetailUiState
    data class Success(val data: LowonganDetail) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

class DaftarLowonganViewModel : ViewModel() {

    // 1. State List & Filter
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Variable Filter Aktif
    var currentFilter by mutableStateOf<String?>(null)
        private set

    // 2. State Detail
    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Idle)
    val detailUiState: StateFlow<DetailUiState> = _detailUiState.asStateFlow()

    init {
        getJobs() // Ambil data awal
    }

    // --- FUNGSI AMBIL LIST (Support Search & Filter) ---
    fun getJobs(filterType: String? = null, searchQuery: String? = null, token: String? = null) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            // 1. UPDATE STATE FILTER (PENTING BUAT HIGHLIGHT UI)
            if (searchQuery.isNullOrEmpty()) {
                currentFilter = filterType
            }

            try {
                // Logika Token: Kalau ada token, kirim ke header "Authorization: Bearer ..."
                // Tapi karena Retrofit kita set up nya beda-beda, kita akali sedikit:
                // Kita harus ubah ApiService getLowongan biar terima Header token (Optional)

                val response = RetrofitClient.instance.getLowongan(
                    search = searchQuery,
                    type = filterType,
                    token = if (token != null) "Bearer $token" else null
                )

                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = HomeUiState.Success(response.body()!!)
                } else {
                    _uiState.value = HomeUiState.Error("Gagal: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Error: ${e.message}")
            }
        }
    }

    // --- FUNGSI AMBIL DETAIL ---
    fun getDetailLowongan(id: String) {
        viewModelScope.launch {
            _detailUiState.value = DetailUiState.Loading
            try {
                val response = RetrofitClient.instance.getDetailLowongan(id)

                if (response.isSuccessful && response.body() != null) {
                    _detailUiState.value = DetailUiState.Success(response.body()!!)
                } else {
                    _detailUiState.value = DetailUiState.Error("Gagal memuat detail")
                }
            } catch (e: Exception) {
                _detailUiState.value = DetailUiState.Error("Error: ${e.message}")
            }
        }
    }
}