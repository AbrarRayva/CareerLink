package com.elevatestudio.careerlink.viewmodel

data class MentoringSession(
    val id: String,
    val nama: String,
    val pekerjaan: String,
    val tanggal: String,
    val jam: String,
    val tempat: String,
    val detail: String,
    val catatanPlatform: String,
    val durasi: String,
    val timeZone: String,
    val mapsCoordinates: String
)