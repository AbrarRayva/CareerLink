package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

data class LowonganItem(
    val id: Int,
    val title: String,
    @SerializedName("company_name") val companyName: String,
    val location: String,
    @SerializedName("job_type") val jobType: String,
    @SerializedName("salary_range") val salaryRange: String,
    @SerializedName("logo_url") val logoUrl: String?,

    // Trik GSON:
    // Kita tampung data asli (yang mungkin null) di variabel private
    @SerializedName("duration")
    private val _duration: String?,

    @SerializedName("semester_requirement")
    private val _semester: String?
) {
    // Lalu kita buat variabel publik yang punya logika anti-null
    val duration: String
        get() = if (_duration.isNullOrEmpty()) "Tidak disebutkan" else _duration

    val semester: String
        get() = if (_semester.isNullOrEmpty()) "Minimal Semester 3" else _semester
}