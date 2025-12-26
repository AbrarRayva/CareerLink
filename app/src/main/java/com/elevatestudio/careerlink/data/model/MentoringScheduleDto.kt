package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

data class MentoringScheduleDto(
    val id: Int,

    @SerializedName("mentor_id")
    val mentorId: Int?,

    @SerializedName("mentor_name")
    val mentorName: String?,

    val datetime: String,
    val location: String?,
    val capacity: Int?,

    val latitude: Double?,
    val longitude: Double?
)
