package com.elevatestudio.careerlink.data.remote

import com.elevatestudio.careerlink.data.model.ApplicationDetail
import com.elevatestudio.careerlink.data.model.AuthRequest
import com.elevatestudio.careerlink.data.model.AuthResponse

import com.elevatestudio.careerlink.data.model.GeneralResponse
import com.elevatestudio.careerlink.data.model.LowonganDetail
import com.elevatestudio.careerlink.data.model.LowonganItem
import com.elevatestudio.careerlink.data.model.NotifikasiItem
import com.elevatestudio.careerlink.data.model.RiwayatItem

import com.elevatestudio.careerlink.data.model.KursusDashboardData
import com.elevatestudio.careerlink.data.model.KursusDetail
import com.elevatestudio.careerlink.data.model.KursusItem
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.RequestBody


interface ApiService {

    @POST("api/auth/register")
    suspend fun register(@Body body: AuthRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body body: AuthRequest): Response<AuthResponse>

    @POST("api/auth/update-fcm")
    suspend fun updateFcmToken(
        @Header("Authorization") token: String,
        @Body data: Map<String, String>
    ): Response<GeneralResponse>

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

    @GET("notifikasi")
    suspend fun getNotifikasi(): Response<List<NotifikasiItem>>

    @DELETE("notifikasi/{id}")
    suspend fun hapusNotifikasi(
        @Path("id") notifikasiId: String
    ): Response<GeneralResponse>



    @GET("/kursus/dashboard")
    suspend fun getKursusDashboard(): Response<KursusDashboardData>

    @GET("/kursus")
    suspend fun getSemuaKursus(
        @Query("search") query: String?
    ): Response<List<KursusItem>>

    @GET("/kursus/{id}")
    suspend fun getDetailKursus(
        @Path("id") kursusId: String
    ): Response<KursusDetail>

    @POST("/kursus/{id}/daftar")
    suspend fun daftarKursus(
        @Path("id") kursusId: String
    ): Response<GeneralResponse>

    @Multipart
    @POST("/kursus/badge/upload")
    suspend fun uploadBadge(
        @Part file: MultipartBody.Part
    ): Response<GeneralResponse>

    @POST("/kursus/badge/scan")
    suspend fun scanBadge(
        @Body qrData: Map<String, String>
    ): Response<GeneralResponse>
}