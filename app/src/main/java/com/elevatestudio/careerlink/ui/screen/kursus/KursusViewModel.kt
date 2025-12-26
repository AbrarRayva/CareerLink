package com.elevatestudio.careerlink.ui.screen.kursus

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.elevatestudio.careerlink.data.model.*
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.utils.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

// --- STATES ---
sealed interface KursusUiState {
    object Loading : KursusUiState
    data class Success(val data: List<Course>) : KursusUiState
    data class Error(val message: String) : KursusUiState
}

sealed interface KursusDetailUiState {
    object Loading : KursusDetailUiState
    data class Success(val data: Course) : KursusDetailUiState
    data class Error(val message: String) : KursusDetailUiState
}

sealed interface SubmissionState {
    object Idle : SubmissionState
    object Loading : SubmissionState
    data class Success(val message: String) : SubmissionState
    data class Error(val message: String) : SubmissionState
}

class KursusViewModel(application: Application) : AndroidViewModel(application) {

   
    private val userPreferences = UserPreferences(application)
    private val apiService = ApiClient.instance

   
    private val _kursusUiState = MutableStateFlow<KursusUiState>(KursusUiState.Loading)
    val kursusUiState = _kursusUiState.asStateFlow()

    private val _detailUiState = MutableStateFlow<KursusDetailUiState>(KursusDetailUiState.Loading)
    val detailUiState = _detailUiState.asStateFlow()

    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionState = _submissionState.asStateFlow()

    private val _stats = MutableStateFlow(UserStats())
    val stats = _stats.asStateFlow()

    private val _badgeState = MutableStateFlow<List<BadgeItem>>(emptyList())
    val badgeState = _badgeState.asStateFlow()

    private val _recommendedCourses = MutableStateFlow<List<Course>>(emptyList())
    val recommendedCourses = _recommendedCourses.asStateFlow()

    private val _myCourses = MutableStateFlow<List<Course>>(emptyList())
    val myCourses = _myCourses.asStateFlow()

   
    private suspend fun getToken(): String {
        val token = userPreferences.authToken.first()
        if (token.isNullOrEmpty()) {
            Log.e("KursusVM", "⚠️ TOKEN KOSONG di UserPreferences")
            return ""
        }
        return token
    }

    fun refreshDashboard() {
        viewModelScope.launch {
            val token = getToken()
            if (token.isEmpty()) return@launch

           
            try {
                val statsRes = apiService.getUserStats("Bearer $token")
                if (statsRes.isSuccessful && statsRes.body()?.success == true) {
                    _stats.value = statsRes.body()!!.data
                }
            } catch (e: Exception) { e.printStackTrace() }

           
            try {
                val recRes = apiService.getRecommendedCourses("Bearer $token")
                if (recRes.isSuccessful && recRes.body()?.success == true) {
                    _recommendedCourses.value = recRes.body()!!.data
                }
            } catch (e: Exception) { e.printStackTrace() }

           
            try {
                val badgeRes = apiService.getMyBadges("Bearer $token")
                if (badgeRes.isSuccessful && badgeRes.body()?.success == true) {
                    val badgeList = badgeRes.body()!!.data
                    _badgeState.value = badgeList

                   
                    Log.d("KursusVM", "Berhasil ambil ${badgeList.size} badge")
                } else {
                    Log.e("KursusVM", "Gagal ambil badge: ${badgeRes.code()}")
                }
            } catch (e: Exception) {
                Log.e("KursusVM", "Error ambil badge", e)
            }
        }
    }

   
    fun enrollCourse(courseId: Int) {
        viewModelScope.launch {
            val token = getToken()
            if (token.isEmpty()) {
                _submissionState.value = SubmissionState.Error("Token Kosong")
                return@launch
            }

            _submissionState.value = SubmissionState.Loading
            try {
                val response = apiService.enrollCourse("Bearer $token", courseId)
                if (response.isSuccessful) {
                    _submissionState.value = SubmissionState.Success("Berhasil Mendaftar!")
                    refreshDashboard()
                } else {
                    _submissionState.value = SubmissionState.Error("Gagal Mendaftar")
                }
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error(e.message ?: "Error Network")
            }
        }
    }

   
    fun uploadBadge(body: MultipartBody.Part) {
        viewModelScope.launch {
            val token = getToken()
            if (token.isEmpty()) {
                _submissionState.value = SubmissionState.Error("Token Kosong")
                return@launch
            }

            _submissionState.value = SubmissionState.Loading
            try {
                val response = apiService.uploadCertificate("Bearer $token", body)
                if (response.isSuccessful) {
                    _submissionState.value = SubmissionState.Success("Berhasil Upload")
                    refreshDashboard()
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Gagal Upload"
                    Log.e("KursusVM", "Upload Fail: $errorMsg")
                    _submissionState.value = SubmissionState.Error("Gagal Upload")
                }
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error(e.message ?: "Error Network")
            }
        }
    }

   
    fun scanQrBadge(courseId: Int) {
        viewModelScope.launch {
            val token = getToken()
            if (token.isEmpty()) return@launch

            _submissionState.value = SubmissionState.Loading
            try {
                val body = mapOf("courseId" to courseId.toString())
                val response = apiService.scanBadge("Bearer $token", body)
                if (response.isSuccessful) {
                    _submissionState.value = SubmissionState.Success("Berhasil Scan")
                    refreshDashboard()
                } else {
                    _submissionState.value = SubmissionState.Error("QR Invalid")
                }
            } catch (e: Exception) { _submissionState.value = SubmissionState.Error("Error") }
        }
    }

   
    fun getDetailKursus(courseId: Int) {
        viewModelScope.launch {
            val token = getToken()
            if (token.isEmpty()) return@launch

            _detailUiState.value = KursusDetailUiState.Loading
            try {
                val response = apiService.getCourseDetail("Bearer $token", courseId)
                if (response.isSuccessful) {
                    response.body()?.data?.let { _detailUiState.value = KursusDetailUiState.Success(it) }
                } else {
                    _detailUiState.value = KursusDetailUiState.Error("Gagal load")
                }
            } catch (e: Exception) { _detailUiState.value = KursusDetailUiState.Error("Error") }
        }
    }

   
    fun getMyCourses(status: String) {
        viewModelScope.launch {
            val token = getToken()
            if (token.isEmpty()) return@launch
            try {
                val cleanStatus = if (status.contains("completed")) "Completed" else "Active"
                val res = apiService.getEnrolledCourses("Bearer $token", cleanStatus)
                if (res.isSuccessful) _myCourses.value = res.body()?.data ?: emptyList()
            } catch (e: Exception) {}
        }
    }

    fun getAllCourses(query: String? = null) {
        viewModelScope.launch {
            val token = getToken()
           
           

            _kursusUiState.value = KursusUiState.Loading
            try {
                val res = apiService.getCourses("Bearer $token")
                if (res.isSuccessful) _kursusUiState.value = KursusUiState.Success(res.body()?.data ?: emptyList())
                else _kursusUiState.value = KursusUiState.Error("Gagal")
            } catch (e: Exception) { _kursusUiState.value = KursusUiState.Error("Error") }
        }
    }

    fun resetSubmissionState() { _submissionState.value = SubmissionState.Idle }
}