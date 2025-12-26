package com.elevatestudio.careerlink.data.remote

import com.elevatestudio.careerlink.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // --- AUTHENTICATION ---
    @POST("api/auth/register")
    suspend fun register(@Body body: AuthRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body body: AuthRequest): Response<AuthResponse>

    @POST("api/auth/update-fcm")
    suspend fun updateFcmToken(
        @Header("Authorization") token: String,
        @Body data: Map<String, String>
    ): Response<GeneralResponse>

    // --- JOBS / LOWONGAN ---
    @GET("api/jobs")
    suspend fun getLowongan(
        @Query("search") search: String? = null,
        @Query("type") type: String? = null,
        @Header("Authorization") token: String? = null
    ): Response<List<LowonganItem>>

    @GET("api/jobs/{id}")
    suspend fun getDetailLowongan(
        @Path("id") lowonganId: String
    ): Response<LowonganDetail>

    @Multipart
    @POST("api/jobs/{id}/apply")
    suspend fun ajukanLowongan(
        @Header("Authorization") token: String,
        @Path("id") lowonganId: String,
        @Part cv: MultipartBody.Part,
        @Part recommendation_letter: MultipartBody.Part,
        @Part portfolio: MultipartBody.Part?,
        @Part("full_name") fullName: RequestBody,
        @Part("date_of_birth") dob: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("education") education: RequestBody,
        @Part("major") major: RequestBody,
        @Part("phone_number") phone: RequestBody,
        @Part("about_me") aboutMe: RequestBody
    ): Response<GeneralResponse>

    @GET("api/jobs/history/my-applications")
    suspend fun getRiwayatLamaran(
        @Header("Authorization") token: String
    ): Response<List<RiwayatItem>>

    @GET("api/jobs/history/detail/{id}")
    suspend fun getDetailLamaran(
        @Header("Authorization") token: String,
        @Path("id") applicationId: String
    ): Response<ApplicationDetail>

    // --- NOTIFIKASI ---
    // Pastikan backend sudah menyediakan endpoint ini, jika belum, sesuaikan dengan backend temanmu
    // Berdasarkan diskusi sebelumnya, kita asumsikan backend sudah support ini.
    @GET("api/notifications")
    suspend fun getNotifikasi(): Response<List<NotifikasiItem>>

    @DELETE("api/notifications/{id}")
    suspend fun hapusNotifikasi(
        @Path("id") notifikasiId: String
    ): Response<GeneralResponse>


    // ==========================================
    // 🔥 MODUL KURSUS (UPDATED) 🔥
    // Sesuai ANDROID_TEAM_API_DOCS.md
    // ==========================================

    // 1. Ambil Kursus Rekomendasi (Dashboard)
    @GET("api/courses/recommended")
    suspend fun getRecommendedCourses(
        @Header("Authorization") token: String? = null // Boleh null, tapi lebih baik diisi
    ): Response<KursusListResponse>

    // 2. Ambil Daftar Semua Kursus (Bisa Search)
    // Endpoint: GET /api/courses
    @GET("api/courses")
    suspend fun getCourses(
        @Query("search") search: String? = null
    ): Response<KursusListResponse>

    // 3. Ambil Detail Kursus
    // Endpoint: GET /api/courses/:courseId
    @GET("api/courses/{id}")
    suspend fun getCourseDetail(
        @Path("id") id: String
    ): Response<KursusDetailResponse>

    // 4. Daftar Kursus (Enroll) - BUTUH TOKEN
    // Endpoint: POST /api/courses/:courseId/enroll
    @POST("api/courses/{id}/enroll")
    suspend fun enrollCourse(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<GeneralResponse>

    // 5. Lihat Kursus Saya (My Enrolled Courses) - BUTUH TOKEN
    // Endpoint: GET /api/courses/enrolled/list
    // Di data/remote/ApiService.kt atau ApiClient.kt

    @GET("api/courses/enrolled/list")
    suspend fun getMyEnrolledCourses(
        @Header("Authorization") token: String,
        @Query("status") status: String? = null // Tambahkan parameter status ini
    ): Response<KursusListResponse>

    // --- BADGE & SCAN ---

    // 6. Lihat Badge Saya (Dashboard) - BUTUH TOKEN
    // Endpoint: GET /api/badges/list
    @GET("api/badges/list")
    suspend fun getMyBadges(
        @Header("Authorization") token: String
    ): Response<BadgeListResponse> // Pastikan buat model BadgeListResponse

    // 7. Scan QR Badge - BUTUH TOKEN
    // Endpoint: POST /api/badges/scan-qr
    @POST("api/badges/scan-qr")
    suspend fun scanBadge(
        @Header("Authorization") token: String,
        @Body body: Map<String, Int> // Body: {"course_id": 5}
    ): Response<GeneralResponse>

    // 8. Statistik Kursus (Dashboard) - BUTUH TOKEN
    // Endpoint: GET /api/courses/stats/overview
    @GET("api/courses/stats/overview")
    suspend fun getCourseStats(
        @Header("Authorization") token: String
    ): Response<StatsResponse> // Perlu buat model StatsResponse jika ingin dipakai

    @Multipart
    @POST("api/badges/upload") // Pastikan backend nanti punya route ini
    suspend fun uploadBadge(
        @Part file: MultipartBody.Part
    ): Response<GeneralResponse>
}