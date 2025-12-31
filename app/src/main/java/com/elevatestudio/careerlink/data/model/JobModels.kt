package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName


data class LowonganItem(
    val id: String,
    val title: String,
    @SerializedName("company_name") val company: String,
    val location: String,
    @SerializedName("job_type") val type: String,
    @SerializedName("salary_range") val salary: String?,
    @SerializedName("created_at") val createdAt: String?
)


data class LowonganDetail(
    val id: String,
    val title: String,
    @SerializedName("company_name") val company: String,
    val location: String,
    @SerializedName("job_type") val type: String,
    val description: String,
    val requirements: String,
    @SerializedName("salary_range") val salary: String?
)


data class RiwayatItem(
    val id: String,
    @SerializedName("job_title") val jobTitle: String,
    @SerializedName("company_name") val companyName: String,
    val status: String,
    @SerializedName("applied_at") val appliedAt: String
)


data class ApplicationDetail(
    val id: String,
    @SerializedName("job_title") val jobTitle: String,
    @SerializedName("company_name") val companyName: String,
    val status: String,
    @SerializedName("applied_at") val appliedAt: String,

   
    @SerializedName("logo_url") val logoUrl: String? = null,
    @SerializedName("full_name") val fullName: String? = "",
    @SerializedName("date_of_birth") val dob: String? = "",
    val gender: String? = "",
    val education: String? = "",
    val major: String? = "",
    @SerializedName("phone_number") val phoneNumber: String? = "",
    @SerializedName("about_me") val aboutMe: String? = "",
    @SerializedName("cv_url") val cvUrl: String? = "",
    @SerializedName("recommendation_letter_url") val letterUrl: String? = "",
    @SerializedName("portfolio_url") val portfolioUrl: String? = ""
)


data class NotifikasiItem(
    val id: String,
    val title: String,
    val message: String,
    @SerializedName("created_at") val createdAt: String?
)