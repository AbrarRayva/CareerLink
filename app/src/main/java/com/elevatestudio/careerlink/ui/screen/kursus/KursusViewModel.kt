package com.elevatestudio.careerlink.ui.screen.kursus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevatestudio.careerlink.data.model.BadgeItem
import com.elevatestudio.careerlink.data.model.CourseStats
import com.elevatestudio.careerlink.data.model.KursusDetail
import com.elevatestudio.careerlink.data.model.KursusItem
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.ui.screen.lowongan.SubmissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

// State UI
sealed class KursusUiState {
    object Loading : KursusUiState()
    data class Success(val data: List<KursusItem>) : KursusUiState()
    data class Error(val message: String) : KursusUiState()
}

sealed class KursusDetailUiState {
    object Loading : KursusDetailUiState()
    data class Success(val data: KursusDetail) : KursusDetailUiState()
    data class Error(val message: String) : KursusDetailUiState()
}

sealed class BadgeUiState {
    object Loading : BadgeUiState()
    data class Success(val data: List<BadgeItem>) : BadgeUiState()
    data class Error(val message: String) : BadgeUiState()
}

class KursusViewModel : ViewModel() {

    // State Data
    private val _rekomendasiState = MutableStateFlow<KursusUiState>(KursusUiState.Loading)
    val rekomendasiState: StateFlow<KursusUiState> = _rekomendasiState

    private val _listKursusState = MutableStateFlow<KursusUiState>(KursusUiState.Loading)
    val listKursusState: StateFlow<KursusUiState> = _listKursusState

    private val _detailState = MutableStateFlow<KursusDetailUiState>(KursusDetailUiState.Loading)
    val detailState: StateFlow<KursusDetailUiState> = _detailState

    private val _badgeState = MutableStateFlow<BadgeUiState>(BadgeUiState.Loading)
    val badgeState: StateFlow<BadgeUiState> = _badgeState

    // Stats State (Default 0)
    private val _statsState = MutableStateFlow<CourseStats>(CourseStats(0, 0, 0))
    val statsState: StateFlow<CourseStats> = _statsState

    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionState: StateFlow<SubmissionState> = _submissionState

    // 1. Refresh Dashboard (Satu Fungsi Saja!)
    fun refreshDashboard(token: String) {
        getStats(token)
        getMyBadges(token)
        getRecommendedCourses(token)
    }

    // 2. Get Stats
    fun getStats(token: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getCourseStats("Bearer $token")
                if (response.isSuccessful && response.body()?.success == true) {
                    _statsState.value = response.body()!!.data
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 3. Rekomendasi (Terima Token)
    fun getRecommendedCourses(token: String? = null) {
        viewModelScope.launch {
            _rekomendasiState.value = KursusUiState.Loading
            try {
                val authHeader = if (token != null) "Bearer $token" else null
                val response = ApiClient.instance.getRecommendedCourses(authHeader)
                if (response.isSuccessful && response.body()?.success == true) {
                    _rekomendasiState.value = KursusUiState.Success(response.body()!!.data)
                } else {
                    _rekomendasiState.value = KursusUiState.Error("Gagal memuat rekomendasi")
                }
            } catch (e: Exception) {
                _rekomendasiState.value = KursusUiState.Error(e.message ?: "Error")
            }
        }
    }

    // 4. Get All Courses
    fun getAllCourses(query: String? = null) {
        viewModelScope.launch {
            _listKursusState.value = KursusUiState.Loading
            try {
                val response = ApiClient.instance.getCourses(query)
                if (response.isSuccessful && response.body()?.success == true) {
                    _listKursusState.value = KursusUiState.Success(response.body()!!.data)
                } else {
                    _listKursusState.value = KursusUiState.Error("Gagal memuat kursus")
                }
            } catch (e: Exception) {
                _listKursusState.value = KursusUiState.Error(e.message ?: "Error")
            }
        }
    }

    // 5. Get Detail
    fun getDetailKursus(id: String) {
        viewModelScope.launch {
            _detailState.value = KursusDetailUiState.Loading
            try {
                val response = ApiClient.instance.getCourseDetail(id)
                if (response.isSuccessful && response.body()?.success == true) {
                    _detailState.value = KursusDetailUiState.Success(response.body()!!.data)
                } else {
                    _detailState.value = KursusDetailUiState.Error("Gagal memuat detail")
                }
            } catch (e: Exception) {
                _detailState.value = KursusDetailUiState.Error(e.message ?: "Error")
            }
        }
    }

    // 6. Get My Badges
    fun getMyBadges(token: String) {
        viewModelScope.launch {
            _badgeState.value = BadgeUiState.Loading
            try {
                val response = ApiClient.instance.getMyBadges("Bearer $token")
                if (response.isSuccessful && response.body()?.success == true) {
                    _badgeState.value = BadgeUiState.Success(response.body()!!.data)
                } else {
                    _badgeState.value = BadgeUiState.Success(emptyList())
                }
            } catch (e: Exception) {
                _badgeState.value = BadgeUiState.Error(e.message ?: "Gagal memuat badge")
            }
        }
    }

    // 7. Daftar Kursus
    fun daftarKursus(kursusId: String, token: String) {
        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            try {
                val response = ApiClient.instance.enrollCourse("Bearer $token", kursusId)
                if (response.isSuccessful) {
                    refreshDashboard(token) // Refresh
                    _submissionState.value = SubmissionState.Success
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    if (response.code() == 400 || errorBody.contains("sudah", true)) {
                        _submissionState.value = SubmissionState.Error("Anda sudah terdaftar.")
                    } else {
                        _submissionState.value = SubmissionState.Error("Gagal mendaftar")
                    }
                }
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // 8. Scan QR Badge
    fun scanQrBadge(token: String, courseId: Int) {
        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            try {
                val body = mapOf("course_id" to courseId)
                val response = ApiClient.instance.scanBadge("Bearer $token", body)
                if (response.isSuccessful && response.body()?.success == true) {
                    refreshDashboard(token) // Refresh
                    _submissionState.value = SubmissionState.Success
                } else {
                    _submissionState.value = SubmissionState.Error("Gagal scan badge")
                }
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error(e.message ?: "Error")
            }
        }
    }

    // 9. Upload Badge
    fun uploadBadge(token: String, file: MultipartBody.Part) {
        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            try {
                val response = ApiClient.instance.uploadBadge(file)
                if (response.isSuccessful) {
                    refreshDashboard(token) // Refresh
                    _submissionState.value = SubmissionState.Success
                } else {
                    _submissionState.value = SubmissionState.Error("Gagal upload")
                }
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error(e.message ?: "Error")
            }
        }
    }

    fun resetSubmissionState() {
        _submissionState.value = SubmissionState.Idle
    }
}