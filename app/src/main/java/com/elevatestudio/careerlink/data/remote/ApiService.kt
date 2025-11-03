package com.elevatestudio.careerlink.data.remote

import com.elevatestudio.careerlink.data.model.AuthRequest
import com.elevatestudio.careerlink.data.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/register")
    suspend fun register(@Body body: AuthRequest): Response<AuthResponse>

    @POST("/login")
    suspend fun login(@Body body: AuthRequest): Response<AuthResponse>
}
