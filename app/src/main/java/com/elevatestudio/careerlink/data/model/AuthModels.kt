package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

// --- 1. MODEL UNTUK REQUEST (Login & Register) ---
data class AuthRequest(
    val email: String,
    val password: String,

    // Tambahan untuk Register (Dibuat nullable/opsional karena Login tidak butuh ini)
    @SerializedName("full_name")
    val fullName: String? = null,

    val role: String? = null
)

// --- 2. MODEL UNTUK RESPONSE (Balikan dari Server) ---
data class AuthResponse(
    val message: String,

    // Token wajib ada kalau sukses
    val token: String = "",

    // Objek User
    val user: UserData
)

// --- 3. MODEL USER DATA (Detail User) ---
data class UserData(
    // PENTING: ViewModel butuh "id" untuk disimpan ke UserPreferences
    val id: Int,

    val email: String,

    @SerializedName("full_name")
    val fullName: String?,

    val role: String?
)