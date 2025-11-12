// Lokasi: data/model/LowonganModels.kt
package com.elevatestudio.careerlink.data.model

import com.google.gson.annotations.SerializedName

// 1. Ini buat data di layar "Daftar Lowongan" (yang list)
data class LowonganItem(
    val id: String,
    val posisi: String,
    val perusahaan: String,
    @SerializedName("logo_url")
    val logoUrl: String,
    @SerializedName("image_url") // <-- TAMBAHAN (biar ada gambar di kanan card)
    val imageUrl: String
)

// 2. Ini buat data di layar "Detail Lowongan"
data class LowonganDetail(
    val id: String,
    val posisi: String,
    val perusahaan: String,
    val lokasi: String,
    @SerializedName("pendidikan_min")
    val pendidikanMin: String, // cth: "S1 | Semester 7"
    @SerializedName("tipe_kerja")
    val tipeKerja: String, // cth: "Magang 6 Bulan | WFO"
    val gaji: String, // cth: "Rp 2.000.000 - Rp 5.000.000"
    val deskripsi: String,
    val responsibilities: List<String>,
    @SerializedName("core_skills")
    val coreSkills: List<String>
)

// 3. Ini data yang kita KIRIM pas ngajuin lowongan
data class AjukanLowonganRequest(
    val namaLengkap: String,
    val tanggalLahir: String,
    val jenisKelamin: String,
    val pendidikan: String,
    val programStudi: String,
    val nomorAktif: String,
    val ceritakanDirimu: String,
    val cvUrl: String, // Nanti di backend, kita kirim link hasil upload-an
    val portofolioUrl: String? = null // Boleh null karena opsional
)

// 4. Ini model buat respon-nya (simple aja)
data class GeneralResponse(
    val message: String
)

// 5. Ini model buat notifikasi
data class NotifikasiItem(
    val id: String,
    val modul: String, // cth: "Lowongan dan Magang"
    val ringkasan: String,
    val timestamp: String
)