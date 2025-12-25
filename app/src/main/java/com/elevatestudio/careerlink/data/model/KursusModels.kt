package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName


data class KursusItem(
    val id: String,
    val penyelenggara: String,
    val judul: String,
    val tipe: String,
    @SerializedName("image_url")
    val imageUrl: String
)

data class KursusDetail(
    val id: String,
    val penyelenggara: String,
    val judul: String,
    val deskripsi: String,
    val lokasi: String,
    val tanggal: String,
    val waktu: String,
    val level: String,
    val kapasitas: String
)

data class BadgeItem(
    val id: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val title: String
)

data class KursusDashboardData(
    val username: String,
    val badges: List<BadgeItem>,
    @SerializedName("statistik_image_url")
    val statistikImageUrl: String,
    val rekomendasi: List<KursusItem>
)