package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

// 1. Request untuk Login & Register
data class AuthRequest(
    val email: String,
    val password: String,
    @SerializedName("full_name") val fullName: String? = null,
    val role: String? = null
)

// 2. Response Utama dari Server
data class AuthResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val token: String?,
    val user: UserResponse?
)

// 3. Data User Detail
data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,

    @SerializedName("photo_profile_url")
    val photoProfileUrl: String? = null,

    @SerializedName("fcm_token")
    val fcmToken: String? = null
)