package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

// 1. Item Kursus (Untuk List & Dashboard)
data class KursusItem(
    val id: Int,
    val title: String,
    @SerializedName("provider_name") val providerName: String,
    @SerializedName("location_type") val locationType: String,
    @SerializedName("image_url") val imageUrl: String?
)

// 2. Detail Kursus
data class KursusDetail(
    val id: Int,
    val title: String,
    @SerializedName("provider_name") val providerName: String,
    val description: String?,
    @SerializedName("location_type") val locationType: String,
    @SerializedName("location_detail") val locationDetail: String?,
    @SerializedName("date_start") val dateStart: String?,
    @SerializedName("image_url") val imageUrl: String?,
    val quota: Int?
)

// --- WRAPPER RESPONSE (PENTING BUAT API SERVICE) ---

// Wrapper untuk List Kursus
data class KursusListResponse(
    val success: Boolean,
    val data: List<KursusItem>
)

// Wrapper untuk Detail Kursus
data class KursusDetailResponse(
    val success: Boolean,
    val data: KursusDetail
)

// --- BADGE MODELS (INI YANG ERROR TADI) ---

data class BadgeItem(
    val id: Int,
    val title: String,
    @SerializedName("image_url") val imageUrl: String?,
    val issuer: String?,
    @SerializedName("obtained_at") val obtainedAt: String?
)

// Wrapper untuk List Badge
data class BadgeListResponse(
    val success: Boolean,
    val data: List<BadgeItem>
)

data class CourseStats(
    @SerializedName("active_courses") val activeCourses: Int = 0,
    @SerializedName("completed_courses") val completedCourses: Int = 0,
    @SerializedName("total_badges") val totalBadges: Int = 0
)

// Wrapper untuk Stats
data class StatsResponse(
    val success: Boolean,
    val data: CourseStats
)