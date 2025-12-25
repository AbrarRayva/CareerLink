// Lokasi: data/model/KursusModels.kt
package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

/**
 * Generic API Response wrapper
 */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val count: Int? = null
)

/**
 * Model untuk item di Daftar Kursus (sesuai backend API /api/courses)
 */
data class KursusItem(
    val id: Int,
    val title: String,
    @SerializedName("provider_name")
    val providerName: String,
    val description: String? = null,
    @SerializedName("location_type")
    val locationType: String, // "Online" or "Offline"
    @SerializedName("location_detail")
    val locationDetail: String? = null,
    @SerializedName("date_start")
    val dateStart: String? = null,
    @SerializedName("date_end")
    val dateEnd: String? = null,
    val quota: Int? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null
)

/**
 * Model untuk Detail Kursus (sesuai backend API /api/courses/:id)
 */
data class KursusDetail(
    val id: Int,
    val title: String,
    @SerializedName("provider_name")
    val providerName: String,
    val description: String,
    @SerializedName("location_type")
    val locationType: String,
    @SerializedName("location_detail")
    val locationDetail: String? = null,
    @SerializedName("date_start")
    val dateStart: String? = null,
    @SerializedName("date_end")
    val dateEnd: String? = null,
    val quota: Int? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null
)

/**
 * Model untuk Enrolled Course (sesuai backend API /api/courses/enrolled/list)
 */
data class EnrolledCourse(
    val id: Int,
    val title: String,
    @SerializedName("provider_name")
    val providerName: String,
    val description: String? = null,
    @SerializedName("location_type")
    val locationType: String,
    val status: String, // "Active", "Completed", etc.
    @SerializedName("enrolled_at")
    val enrolledAt: String? = null
)

/**
 * Model untuk Badge (sesuai backend API /api/badges/list)
 */
data class BadgeItem(
    val id: Int,
    val title: String,
    val description: String? = null,
    @SerializedName("obtained_at")
    val obtainedAt: String? = null,
    @SerializedName("image_url")
    val imageUrl: String? = null
)

/**
 * Model untuk Badge Response setelah scan QR
 */
data class BadgeResponse(
    @SerializedName("badge_id")
    val badgeId: Int,
    @SerializedName("badge_title")
    val badgeTitle: String,
    @SerializedName("course_id")
    val courseId: Int,
    @SerializedName("course_title")
    val courseTitle: String,
    @SerializedName("obtained_at")
    val obtainedAt: String
)

/**
 * Model untuk Course Stats (sesuai backend API /api/courses/stats/overview)
 */
data class CourseStats(
    @SerializedName("total_courses")
    val totalCourses: Int,
    @SerializedName("active_courses")
    val activeCourses: Int,
    @SerializedName("completed_courses")
    val completedCourses: Int,
    @SerializedName("cancelled_courses")
    val cancelledCourses: Int,
    @SerializedName("total_hours")
    val totalHours: Int? = null
)

/**
 * Model untuk Course Recap (sesuai backend API /api/courses/recap/all)
 */
data class CourseRecap(
    @SerializedName("course_id")
    val courseId: Int,
    val title: String,
    @SerializedName("provider_name")
    val providerName: String,
    val status: String,
    @SerializedName("enrolled_at")
    val enrolledAt: String? = null,
    @SerializedName("completed_at")
    val completedAt: String? = null,
    val badges: List<BadgeItem> = emptyList()
)

/**
 * Model untuk Enrollment Response
 */
data class EnrollResponse(
    @SerializedName("enrollmentId")
    val enrollmentId: Int? = null,
    @SerializedName("course_id")
    val courseId: Int? = null,
    @SerializedName("course_title")
    val courseTitle: String? = null,
    @SerializedName("enrolled_at")
    val enrolledAt: String? = null
)

/**
 * Model untuk Dashboard Kursus (untuk kompatibilitas dengan UI yang ada)
 */
data class KursusDashboardData(
    val username: String,
    val badges: List<BadgeItem>,
    @SerializedName("statistik_image_url")
    val statistikImageUrl: String? = null,
    val rekomendasi: List<KursusItem>
)

/**
 * Request untuk scan QR badge
 */
data class ScanQRRequest(
    @SerializedName("course_id")
    val courseId: Int
)