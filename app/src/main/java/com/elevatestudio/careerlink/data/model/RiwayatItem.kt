package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

data class RiwayatItem(
    @SerializedName("application_id") val applicationId: Int,
    @SerializedName("job_id") val jobId: Int,
    val title: String,
    @SerializedName("company_name") val companyName: String,
    @SerializedName("logo_url") val logoUrl: String?,
    val status: String, // Pending, Accepted, Rejected
    @SerializedName("applied_at") val appliedAt: String
)