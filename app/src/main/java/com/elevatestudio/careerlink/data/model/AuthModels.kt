package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName


data class AuthRequest(
    val email: String,
    val password: String,
    @SerializedName("full_name") val fullName: String? = null,
    val role: String? = null
)


data class AuthResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val token: String?,
    val user: UserResponse?
)


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