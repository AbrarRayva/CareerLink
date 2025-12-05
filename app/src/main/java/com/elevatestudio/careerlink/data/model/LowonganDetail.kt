package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

data class LowonganDetail(
    val id: Int,
    val title: String,

    @SerializedName("company_name")
    val companyName: String,

    @SerializedName("logo_url")
    val logoUrl: String?,

    val location: String,

    @SerializedName("job_type")
    val jobType: String,

    @SerializedName("salary_range")
    val salaryRange: String,

    // Tambahan detail yang tidak ada di list biasa
    val description: String?,
    val requirements: String?,

    @SerializedName("created_at")
    val createdAt: String
)