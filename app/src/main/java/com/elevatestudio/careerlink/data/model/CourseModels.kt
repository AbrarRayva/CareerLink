package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName


data class CourseResponse(
    val success: Boolean,
    val message: String?,
    val data: List<Course>
)

data class SingleCourseResponse(
    val success: Boolean,
    val message: String?,
    val data: Course
)

data class StatsResponse(
    val success: Boolean,
    val data: UserStats
)

data class GeneralResponse(
    val success: Boolean,
    val message: String
)


data class Course(
    val id: Int,
    val title: String,
    val description: String?,
    val price: Double? = 0.0,
    val image: String?,
   
    val role: String? = "Elevate Academy",
    @SerializedName("location_type") val locationType: String? = "Online",
    @SerializedName("enrollment_status") val enrollmentStatus: String? = null,
    val status: String? = null,
    val mentor: String? = "Mentor CareerLink"
)

data class UserStats(
    val activeCourses: Int = 0,
    val completedCourses: Int = 0,
    val totalBadges: Int = 0
)

data class BadgeItem(
    val id: Int,
    @SerializedName("title") val name: String,
    @SerializedName("image_url") val imageUrl: String?
)

data class BadgeResponse(
    val success: Boolean,
    val message: String?,
    val data: List<BadgeItem>
)