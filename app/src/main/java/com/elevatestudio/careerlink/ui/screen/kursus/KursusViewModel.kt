// Lokasi: ui/screen/kursus/KursusViewModel.kt
package com.elevatestudio.careerlink.ui.screen.kursus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevatestudio.careerlink.data.model.BadgeItem
import com.elevatestudio.careerlink.data.model.CourseStats
import com.elevatestudio.careerlink.data.model.CourseRecap
import com.elevatestudio.careerlink.data.model.EnrolledCourse
import com.elevatestudio.careerlink.data.model.EnrollResponse
import com.elevatestudio.careerlink.data.model.KursusDetail
import com.elevatestudio.careerlink.data.model.KursusItem
import com.elevatestudio.careerlink.data.model.ScanQRRequest
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.ui.screen.lowongan.SubmissionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KursusViewModel : ViewModel() {

    // State untuk nanganin pop-up (Loading, Success, Error)
    private val _submissionState = MutableStateFlow<SubmissionState>(SubmissionState.Idle)
    val submissionState: StateFlow<SubmissionState> = _submissionState.asStateFlow()

    // State untuk list kursus
    private val _courses = MutableStateFlow<List<KursusItem>>(emptyList())
    val courses: StateFlow<List<KursusItem>> = _courses.asStateFlow()

    // State untuk recommended courses
    private val _recommendedCourses = MutableStateFlow<List<KursusItem>>(emptyList())
    val recommendedCourses: StateFlow<List<KursusItem>> = _recommendedCourses.asStateFlow()

    // State untuk course detail
    private val _courseDetail = MutableStateFlow<KursusDetail?>(null)
    val courseDetail: StateFlow<KursusDetail?> = _courseDetail.asStateFlow()

    // State untuk enrolled courses
    private val _enrolledCourses = MutableStateFlow<List<EnrolledCourse>>(emptyList())
    val enrolledCourses: StateFlow<List<EnrolledCourse>> = _enrolledCourses.asStateFlow()

    // State untuk badges
    private val _badges = MutableStateFlow<List<BadgeItem>>(emptyList())
    val badges: StateFlow<List<BadgeItem>> = _badges.asStateFlow()

    // State untuk course stats
    private val _courseStats = MutableStateFlow<CourseStats?>(null)
    val courseStats: StateFlow<CourseStats?> = _courseStats.asStateFlow()

    // State untuk loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State untuk error message
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Get semua kursus dengan filter optional
     */
    fun getCourses(locationType: String? = null, providerName: String? = null, search: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = ApiClient.instance.getCourses(locationType, providerName, search)
                if (response.isSuccessful && response.body()?.success == true) {
                    _courses.value = response.body()?.data ?: emptyList()
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal mengambil data kursus"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Get recommended courses
     */
    fun getRecommendedCourses(limit: Int? = null) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getRecommendedCourses(limit)
                if (response.isSuccessful && response.body()?.success == true) {
                    _recommendedCourses.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                // Handle error silently for recommended courses
            }
        }
    }

    /**
     * Get detail kursus
     */
    fun getCourseDetail(courseId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = ApiClient.instance.getCourseDetail(courseId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _courseDetail.value = response.body()?.data
                } else {
                    _errorMessage.value = response.body()?.message ?: "Gagal mengambil detail kursus"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Get enrolled courses
     */
    fun getEnrolledCourses() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.instance.getEnrolledCourses()
                if (response.isSuccessful && response.body()?.success == true) {
                    _enrolledCourses.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Get badges
     */
    fun getBadges() {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getBadges()
                if (response.isSuccessful && response.body()?.success == true) {
                    _badges.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }

    /**
     * Get course stats
     */
    fun getCourseStats() {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.getCourseStats()
                if (response.isSuccessful && response.body()?.success == true) {
                    _courseStats.value = response.body()?.data
                }
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }

    /**
     * Dipanggil pas klik "DAFTAR SEKARANG"
     */
    fun daftarKursus(courseId: Int) {
        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            try {
                val response = ApiClient.instance.enrollCourse(courseId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _submissionState.value = SubmissionState.Success
                    // Refresh enrolled courses list
                    getEnrolledCourses()
                } else {
                    _submissionState.value = SubmissionState.Error(
                        response.body()?.message ?: "Gagal mendaftar ke kursus"
                    )
                }
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Batalkan pendaftaran kursus
     */
    fun unenrollCourse(courseId: Int) {
        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            try {
                val response = ApiClient.instance.unenrollCourse(courseId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _submissionState.value = SubmissionState.Success
                    // Refresh enrolled courses list
                    getEnrolledCourses()
                } else {
                    _submissionState.value = SubmissionState.Error(
                        response.body()?.message ?: "Gagal membatalkan pendaftaran"
                    )
                }
            } catch (e: Exception) {
                _submissionState.value = SubmissionState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Dipanggil pas selesai scan QR
     */
    fun scanSertifikat(courseId: Int) {
        viewModelScope.launch {
            _submissionState.value = SubmissionState.Loading
            try {
                val response = ApiClient.instance.scanQRBadge(ScanQRRequest(courseId))
                if (response.isSuccessful && response.body()?.success == true) {
                    _submissionState.value = SubmissionState.Success
                    // Refresh badges list
                    getBadges()
                } else {
                    _submissionState.value = SubmissionState.Error(
                        response.body()?.message ?: "QR Code tidak valid"
                    )
                }
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

    /**
     * Clear error message
     */
    fun clearError() {
        _errorMessage.value = null
    }
}