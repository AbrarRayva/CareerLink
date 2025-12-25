// Lokasi: data/remote/ApiService.kt
package com.elevatestudio.careerlink.data.remote

// --- IMPORT UNTUK OTENTIKASI ---
import com.elevatestudio.careerlink.data.model.AuthRequest
import com.elevatestudio.careerlink.data.model.AuthResponse
import com.elevatestudio.careerlink.data.model.RegisterRequest

// --- IMPORT UNTUK MODUL LOWONGAN ---
import com.elevatestudio.careerlink.data.model.AjukanLowonganRequest
import com.elevatestudio.careerlink.data.model.GeneralResponse
import com.elevatestudio.careerlink.data.model.LowonganDetail
import com.elevatestudio.careerlink.data.model.LowonganItem
import com.elevatestudio.careerlink.data.model.NotifikasiItem

// --- IMPORT UNTUK MODUL KURSUS ---
import com.elevatestudio.careerlink.data.model.ApiResponse
import com.elevatestudio.careerlink.data.model.BadgeItem
import com.elevatestudio.careerlink.data.model.BadgeResponse
import com.elevatestudio.careerlink.data.model.CourseStats
import com.elevatestudio.careerlink.data.model.CourseRecap
import com.elevatestudio.careerlink.data.model.EnrolledCourse
import com.elevatestudio.careerlink.data.model.EnrollResponse
import com.elevatestudio.careerlink.data.model.KursusDetail
import com.elevatestudio.careerlink.data.model.KursusItem
import com.elevatestudio.careerlink.data.model.ScanQRRequest
// --- SELESAI IMPORT KURSUS ---

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    // --- Otentikasi ---
    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<ApiResponse<com.elevatestudio.careerlink.data.model.UserData>>

    @POST("api/auth/login")
    suspend fun login(@Body body: AuthRequest): Response<ApiResponse<com.elevatestudio.careerlink.data.model.UserData>>


    // --- Modul Lowongan (keep existing for compatibility) ---
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
     * GET /api/courses - List semua kursus dengan filter optional
     */
    @GET("api/courses")
    suspend fun getCourses(
        @Query("location_type") locationType: String? = null,
        @Query("provider_name") providerName: String? = null,
        @Query("search") search: String? = null
    ): Response<ApiResponse<List<KursusItem>>>

    /**
     * GET /api/courses/:courseId - Detail satu kursus
     */
    @GET("api/courses/{courseId}")
    suspend fun getCourseDetail(
        @Path("courseId") courseId: Int
    ): Response<ApiResponse<KursusDetail>>

    /**
     * GET /api/courses/recommended - Kursus rekomendasi
     */
    @GET("api/courses/recommended")
    suspend fun getRecommendedCourses(
        @Query("limit") limit: Int? = null
    ): Response<ApiResponse<List<KursusItem>>>

    /**
     * POST /api/courses/:courseId/enroll - Daftar ke kursus
     */
    @POST("api/courses/{courseId}/enroll")
    suspend fun enrollCourse(
        @Path("courseId") courseId: Int
    ): Response<ApiResponse<EnrollResponse>>

    /**
     * DELETE /api/courses/:courseId/unenroll - Batalkan pendaftaran
     */
    @DELETE("api/courses/{courseId}/unenroll")
    suspend fun unenrollCourse(
        @Path("courseId") courseId: Int
    ): Response<ApiResponse<GeneralResponse>>

    /**
     * GET /api/courses/enrolled/list - Kursus yang sudah didaftar
     */
    @GET("api/courses/enrolled/list")
    suspend fun getEnrolledCourses(): Response<ApiResponse<List<EnrolledCourse>>>

    /**
     * GET /api/courses/stats/overview - Statistik kursus
     */
    @GET("api/courses/stats/overview")
    suspend fun getCourseStats(): Response<ApiResponse<CourseStats>>

    /**
     * GET /api/courses/recap/all - Rekap lengkap kursus dengan badge
     */
    @GET("api/courses/recap/all")
    suspend fun getCourseRecap(): Response<ApiResponse<List<CourseRecap>>>

    /**
     * GET /api/badges/list - List badge yang sudah diperoleh
     */
    @GET("api/badges/list")
    suspend fun getBadges(): Response<ApiResponse<List<BadgeItem>>>

    /**
     * POST /api/badges/scan-qr - Scan QR untuk mendapatkan badge
     */
    @POST("api/badges/scan-qr")
    suspend fun scanQRBadge(
        @Body body: ScanQRRequest
    ): Response<ApiResponse<BadgeResponse>>

    /**
     * GET /api/badges/stats - Statistik badge
     */
    @GET("api/badges/stats")
    suspend fun getBadgeStats(): Response<ApiResponse<Map<String, Int>>>
}