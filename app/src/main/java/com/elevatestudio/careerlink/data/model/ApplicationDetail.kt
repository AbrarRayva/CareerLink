package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

data class ApplicationDetail(
    val id: Int,
    val status: String,
    @SerializedName("job_title") val jobTitle: String,
    @SerializedName("company_name") val companyName: String,
    @SerializedName("logo_url") val logoUrl: String?,
    @SerializedName("applied_at") val appliedAt: String,

    @SerializedName("full_name") val fullName: String,
    @SerializedName("date_of_birth") val dob: String,
    val gender: String,
    val education: String,
    val major: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("about_me") val aboutMe: String,


    @SerializedName("cv_url") val cvUrl: String,
    @SerializedName("recommendation_letter_url") val letterUrl: String,
    @SerializedName("portfolio_url") val portfolioUrl: String?
)