package com.elevatestudio.careerlink.data.model

data class AuthRequest(
    val username: String,
    val password: String
)

data class UserData(
    val username: String
)

data class AuthResponse(
    val message: String,
    val token: String? = null,
    val user: UserData? = null
)