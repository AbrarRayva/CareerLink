package com.elevatestudio.careerlink.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MentoringViewModel : ViewModel() {

    // Dummy Data
    private val allSessions = listOf(
        MentoringSession(
            id = "s001",
            nama = "Jack",
            pekerjaan = "Microsoft",
            tanggal = "Saturday, 18 Oct 2025",
            jam = "04.00 pm",
            tempat = "Cafe Dari Sini",
            detail = "Sesi ini akan berfokus pada review mendalam 1-2 studi kasus terbaik Anda. Kita akan bahas bagaimana menyusun flow cerita yang menarik, cara mengukur dampak desain, serta simulasi pertanyaan wawancara teknis terkait proses desain yang Anda lakukan.",
            catatanPlatform = "Platform: Cafe Dari Sini",
            durasi = "60 Menit (1 jam)",
            timeZone = "WIB (Waktu Indonesia Barat)",
            mapsCoordinates = "Koordinat Lokasi Jack"
        ),
        MentoringSession(
            id = "s002",
            nama = "Rain",
            pekerjaan = "Google",
            tanggal = "Thursday, 2 Oct 2025",
            jam = "10.00 am",
            tempat = "Kopi Kita Sutomo",
            detail = "Membahas persiapan karir sebagai UI/UX Designer di perusahaan teknologi besar. Fokus pada design system dan user research.",
            catatanPlatform = "Platform: Kopi Kita Sutomo",
            durasi = "90 Menit (1.5 jam)",
            timeZone = "WIB (Waktu Indonesia Barat)",
            mapsCoordinates = "Koordinat Lokasi Rain"
        )
    )

    // State untuk daftar sesi yang ditampilkan di UI (Cari Jadwal Mentoring)
    private val _mentoringSessions = MutableStateFlow(allSessions)
    val mentoringSessions: StateFlow<List<MentoringSession>> = _mentoringSessions


    // Mendapatkan detail sesi berdasarkan ID.
    fun getSessionById(sessionId: String?): MentoringSession? {
        return allSessions.find { it.id == sessionId }
    }
}