package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    val email: String,
    val password: String
)

// For register
data class RegisterRequest(
    val full_name: String,
    val email: String,
    val password: String,
    val role: String = "student"
)

data class UserData(
    val id: Int? = null,
    @SerializedName("full_name")
    val fullName: String? = null,
    val email: String? = null,
    val role: String? = null,
    val token: String? = null
)

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val data: UserData? = null
)
