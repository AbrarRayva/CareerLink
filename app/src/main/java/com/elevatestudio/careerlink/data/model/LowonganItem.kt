// Lokasi: data/model/LowonganItem.kt
package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

data class LowonganItem(
    val id: Int, // atau String, sesuaikan dengan database (biasanya Int dari MySQL)
    val title: String,

    @SerializedName("company_name")
    val companyName: String,

    val location: String,

    @SerializedName("job_type")
    val jobType: String, // Full Time / Part Time

    @SerializedName("salary_range")
    val salaryRange: String,

    @SerializedName("logo_url")
    val logoUrl: String? = null,

    @SerializedName("created_at")
    val createdAt: String
)