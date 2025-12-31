package com.elevatestudio.careerlink.data.remote

import com.elevatestudio.careerlink.data.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

   
   
   

    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: AuthRequest): Response<AuthResponse>

    @POST("api/auth/update-fcm")
    suspend fun updateFcmToken(
        @Header("Authorization") token: String,
        @Body data: Map<String, String>
    ): Response<GeneralResponse>

   
   
   

   
    @GET("api/courses")
    suspend fun getCourses(@Header("Authorization") token: String?): Response<CourseResponse>

    @GET("api/courses/recommended")
    suspend fun getRecommendedCourses(@Header("Authorization") token: String): Response<CourseResponse>

    @GET("api/courses/stats")
    suspend fun getUserStats(@Header("Authorization") token: String): Response<StatsResponse>

    @GET("api/courses/enrolled/list")
    suspend fun getEnrolledCourses(
        @Header("Authorization") token: String,
        @Query("status") status: String
    ): Response<CourseResponse>

    @GET("api/courses/{id}")
    suspend fun getCourseDetail(
        @Header("Authorization") token: String,
        @Path("id") courseId: Int
    ): Response<SingleCourseResponse>

    @POST("api/courses/enroll/{id}")
    suspend fun enrollCourse(
        @Header("Authorization") token: String,
        @Path("id") courseId: Int
    ): Response<GeneralResponse>


    @GET("api/badges/list")
    suspend fun getMyBadges(@Header("Authorization") token: String): Response<BadgeResponse>

    @Multipart
    @POST("api/badges/upload")
    suspend fun uploadCertificate(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Response<GeneralResponse>

    @POST("api/badges/scan-qr")
    suspend fun scanBadge(
        @Header("Authorization") token: String,
        @Body data: Map<String, String>
    ): Response<GeneralResponse>



    @GET("api/jobs")
    suspend fun getLowongan(
        @Query("search") search: String?,
        @Query("type") type: String?,
        @Header("Authorization") token: String?
    ): Response<List<LowonganItem>>

    @GET("api/jobs/{id}")
    suspend fun getDetailLowongan(
        @Header("Authorization") token: String?,
        @Path("id") lowonganId: String
    ): Response<LowonganDetail>

    @Multipart
    @POST("api/jobs/{id}/apply")
    suspend fun ajukanLowongan(
        @Header("Authorization") token: String,

        @Path("id") lowonganId: String,

       
        @Part cv: MultipartBody.Part?,
        @Part recommendation_letter: MultipartBody.Part? = null,
        @Part portfolio: MultipartBody.Part? = null,

        @Part("full_name") fullName: okhttp3.RequestBody?,
        @Part("date_of_birth") dob: okhttp3.RequestBody?,
        @Part("gender") gender: okhttp3.RequestBody?,
        @Part("education") education: okhttp3.RequestBody?,
        @Part("major") major: okhttp3.RequestBody?,
        @Part("phone_number") phone: okhttp3.RequestBody?,
        @Part("about_me") aboutMe: okhttp3.RequestBody?
    ): Response<GeneralResponse>

    @GET("api/jobs/applications")
    suspend fun getRiwayatLamaran(@Header("Authorization") token: String): Response<List<RiwayatItem>>

    @GET("api/jobs/applications/{id}")
    suspend fun getDetailLamaran(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ApplicationDetail>

    @GET("api/notifications")
    suspend fun getNotifikasi(@Header("Authorization") token: String): Response<List<NotifikasiItem>>

    @DELETE("api/notifications/{id}")
    suspend fun hapusNotifikasi(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>

}