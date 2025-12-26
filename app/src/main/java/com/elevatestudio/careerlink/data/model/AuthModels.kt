package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    val email: String,
    val password: String,

    @SerializedName("full_name")
    val fullName: String? = null,

    val role: String? = null
)

data class AuthResponse(
    val message: String,
    val token: String = "",
    val user: UserData
)

data class UserData(
    val id: Int,
    val email: String,

    @SerializedName("full_name")
    val fullName: String?,
    val role: String?
)