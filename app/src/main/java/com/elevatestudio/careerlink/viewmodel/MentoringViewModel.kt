package com.elevatestudio.careerlink.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevatestudio.careerlink.data.remote.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MentoringViewModel : ViewModel() {

    // ✅ Dummy awal (Padang - sekitar Unand) biar UI tetap ada isi
    private val dummySessions = listOf(
        MentoringSession(
            id = "s001",
            nama = "Rizki Pratama",
            pekerjaan = "Software Engineer",
            tanggal = "Senin, 5 Januari 2026",
            jam = "10.00 - 11.00",
            tempat = "Perpustakaan Universitas Andalas, Limau Manis, Padang",
            detail = "Review CV dan portofolio, tips interview, dan penentuan roadmap karir 3 bulan ke depan.",
            catatanPlatform = "Pertemuan Offline (bawa laptop & CV).",
            durasi = "60 Menit",
            timeZone = "WIB (GMT+7)",
            // ✅ format "lat,lng" supaya gampang dipakai MapsSection lama kamu
            mapsCoordinates = "-0.9147,100.4570"
        ),
        MentoringSession(
            id = "s002",
            nama = "Nabila Sari",
            pekerjaan = "UI/UX Designer",
            tanggal = "Selasa, 6 Januari 2026",
            jam = "13.00 - 14.00",
            tempat = "Kantin Pusat Unand, Limau Manis, Padang",
            detail = "Review case study, cara menyusun storytelling portofolio, dan latihan presentasi.",
            catatanPlatform = "Pertemuan Offline (siapkan 1 case study terbaik).",
            durasi = "60 Menit",
            timeZone = "WIB (GMT+7)",
            mapsCoordinates = "-0.9152,100.4587"
        )
    )

    private val _mentoringSessions = MutableStateFlow(dummySessions)
    val mentoringSessions: StateFlow<List<MentoringSession>> = _mentoringSessions.asStateFlow()

    init {
        // ✅ otomatis coba ambil data backend
        fetchSessionsFromBackend()
    }

    fun fetchSessionsFromBackend() {
        viewModelScope.launch {
            try {
                // Endpoint backend kamu: GET /api/mentoring/schedules
                val dtoList = ApiClient.mentoringApi.getMentoringSchedules()

                // map DTO -> UI model
                val mapped = dtoList.map { dto ->
                    MentoringSession(
                        id = dto.id.toString(), // kalau backend id Int
                        nama = dto.mentor_name ?: "Mentor",
                        pekerjaan = "Mentor", // kalau belum ada field pekerjaan, isi default
                        tanggal = dto.datetime, // nanti kalau mau format cantik, kita rapihin
                        jam = "",               // kalau backend datetime gabung jam, bisa dipisah belakangan
                        tempat = dto.location ?: "-",
                        detail = "Sesi mentoring karir dan konseling.",
                        catatanPlatform = "Pertemuan Offline/Online sesuai lokasi.",
                        durasi = "60 Menit",
                        timeZone = "WIB (GMT+7)",
                        mapsCoordinates = if (dto.latitude != null && dto.longitude != null)
                            "${dto.latitude},${dto.longitude}"
                        else null
                    )
                }

                if (mapped.isNotEmpty()) {
                    _mentoringSessions.value = mapped
                }
            } catch (e: Exception) {
                // ✅ kalau gagal konek backend, biarin dummy tampil
                e.printStackTrace()
            }
        }
    }

    fun getSessionById(sessionId: String?): MentoringSession? {
        if (sessionId.isNullOrBlank()) return null
        // ✅ cari dari state yang sedang tampil, bukan dari list awal
        return _mentoringSessions.value.firstOrNull { it.id == sessionId }
    }
}
