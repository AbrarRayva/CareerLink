// Lokasi: data/model/KursusModels.kt
package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

/**
 * Model untuk item di Daftar Kursus & Rekomendasi
 */
data class KursusItem(
    val id: String,
    val penyelenggara: String,
    val judul: String,
    val tipe: String, // "Online" or "Offline"
    @SerializedName("image_url")
    val imageUrl: String
)

/**
 * Model untuk layar Detail Kursus (Daftar Kelas)
 */
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

/**
 * Model untuk "Course dan Badge Saya" di Dashboard
 */
data class BadgeItem(
    val id: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val title: String
)

/**
 * Model untuk nampung semua data di Dashboard Kursus
 */
data class KursusDashboardData(
    val username: String,
    val badges: List<BadgeItem>,
    @SerializedName("statistik_image_url")
    val statistikImageUrl: String,
    val rekomendasi: List<KursusItem>
)