// Lokasi: data/remote/ApiService.kt
package com.elevatestudio.careerlink.data.remote

// --- IMPORT UNTUK OTENTIKASI ---
import com.elevatestudio.careerlink.data.model.AuthRequest
import com.elevatestudio.careerlink.data.model.AuthResponse

// --- IMPORT UNTUK MODUL LOWONGAN ---
import com.elevatestudio.careerlink.data.model.AjukanLowonganRequest
import com.elevatestudio.careerlink.data.model.GeneralResponse
import com.elevatestudio.careerlink.data.model.LowonganDetail
import com.elevatestudio.careerlink.data.model.LowonganItem
import com.elevatestudio.careerlink.data.model.NotifikasiItem

// --- IMPORT UNTUK MODUL KURSUS ---
import com.elevatestudio.careerlink.data.model.KursusDashboardData
import com.elevatestudio.careerlink.data.model.KursusDetail
import com.elevatestudio.careerlink.data.model.KursusItem
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part
// --- SELESAI IMPORT KURSUS ---

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    // --- Otentikasi ---
    @POST("/register")
    suspend fun register(@Body body: AuthRequest): Response<AuthResponse>

    @POST("/login")
    suspend fun login(@Body body: AuthRequest): Response<AuthResponse>


    // --- Modul Lowongan ---
    @GET("/lowongan")
    suspend fun getLowongan(
        @Query("search") query: String? = null,
        @Query("tipe") tipe: String? = null
    ): Response<List<LowonganItem>>

    @GET("/lowongan/{id}")
    suspend fun getDetailLowongan(
        @Path("id") lowonganId: String
    ): Response<LowonganDetail>

    @POST("/lowongan/{id}/ajukan")
    suspend fun ajukanLowongan(
        @Path("id") lowonganId: String,
        @Body body: AjukanLowonganRequest
    ): Response<GeneralResponse>

    @GET("/notifikasi")
    suspend fun getNotifikasi(): Response<List<NotifikasiItem>>

    @DELETE("/notifikasi/{id}")
    suspend fun hapusNotifikasi(
        @Path("id") notifikasiId: String
    ): Response<GeneralResponse>


    // --- MODUL KURSUS ---

    /**
     * 1. Dapetin data dashboard kursus
     */
    @GET("/kursus/dashboard")
    suspend fun getKursusDashboard(): Response<KursusDashboardData>

    /**
     * 2. Dapetin list kursus (buat search)
     */
    @GET("/kursus")
    suspend fun getSemuaKursus(
        @Query("search") query: String?
    ): Response<List<KursusItem>>

    /**
     * 3. Dapetin detail satu kursus
     */
    @GET("/kursus/{id}")
    suspend fun getDetailKursus(
        @Path("id") kursusId: String
    ): Response<KursusDetail>

    /**
     * 4. Daftar ke kursus
     */
    @POST("/kursus/{id}/daftar")
    suspend fun daftarKursus(
        @Path("id") kursusId: String
    ): Response<GeneralResponse>

    /**
     * 5. Upload badge (sertifikat) via file
     */
    @Multipart
    @POST("/kursus/badge/upload")
    suspend fun uploadBadge(
        @Part file: MultipartBody.Part
    ): Response<GeneralResponse>

    /**
     * 6. Upload badge (sertifikat) via scan QR
     */
    @POST("/kursus/badge/scan")
    suspend fun scanBadge(
        @Body qrData: Map<String, String>
    ): Response<GeneralResponse>
}