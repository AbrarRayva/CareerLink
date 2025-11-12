package com.elevatestudio.careerlink.data.remote

import com.elevatestudio.careerlink.data.model.AjukanLowonganRequest
import com.elevatestudio.careerlink.data.model.AuthRequest
import com.elevatestudio.careerlink.data.model.AuthResponse
import com.elevatestudio.careerlink.data.model.GeneralResponse
import com.elevatestudio.careerlink.data.model.LowonganDetail
import com.elevatestudio.careerlink.data.model.LowonganItem
import com.elevatestudio.careerlink.data.model.NotifikasiItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/register")
    suspend fun register(@Body body: AuthRequest): Response<AuthResponse>

    @POST("/login")
    suspend fun login(@Body body: AuthRequest): Response<AuthResponse>

    // 1. Dapetin semua lowongan (bisa difilter)
    @GET("/lowongan")
    suspend fun getLowongan(
        @Query("search") query: String? = null, // Buat search bar
        @Query("tipe") tipe: String? = null // Buat filter chip (cth: "magang")
    ): Response<List<LowonganItem>>

    // 2. Dapetin detail satu lowongan
    @GET("/lowongan/{id}")
    suspend fun getDetailLowongan(
        @Path("id") lowonganId: String
    ): Response<LowonganDetail>

    // 3. Ngirim form ajuan lowongan
    // Di Express/backend, kamu perlu setup (misal: Multer) buat nerima file
    // Tapi di app, kita bisa kirim link-nya aja (lebih gampang)
    @POST("/lowongan/{id}/ajukan")
    suspend fun ajukanLowongan(
        @Path("id") lowonganId: String,
        @Body body: AjukanLowonganRequest
    ): Response<GeneralResponse>

    // 4. Dapetin semua notifikasi
    @GET("/notifikasi")
    suspend fun getNotifikasi(): Response<List<NotifikasiItem>>

    // 5. Hapus satu notifikasi
    @DELETE("/notifikasi/{id}")
    suspend fun hapusNotifikasi(
        @Path("id") notifikasiId: String
    ): Response<GeneralResponse>
}
