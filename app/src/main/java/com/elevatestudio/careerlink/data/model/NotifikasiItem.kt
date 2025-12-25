package com.elevatestudio.careerlink.data.model

data class NotifikasiItem(
    val id: Int,
    val title: String,
    val message: String,
    val date: String,
    val jobId: Int?
)