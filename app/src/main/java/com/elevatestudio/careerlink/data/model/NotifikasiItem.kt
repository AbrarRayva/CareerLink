package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

data class NotifikasiItem(
    val id: Int,
    val title: String,
    val message: String,

    @SerializedName("created_at")
    val date: String,

    @SerializedName("is_read")
    val isRead: Boolean = false
)