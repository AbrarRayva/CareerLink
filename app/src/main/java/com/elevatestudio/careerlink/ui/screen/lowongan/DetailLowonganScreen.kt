// Lokasi: ui/screen/lowongan/DetailLowonganScreen.kt
package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elevatestudio.careerlink.data.model.LowonganDetail
import com.elevatestudio.careerlink.ui.components.PrimaryButton
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen

// Data dummy disesuaikan dengan Model LowonganDetail.kt
val dummyDetail = LowonganDetail(
    id = 1, // HARUS INT
    title = "Software Developer Intern", // BUKAN posisi
    companyName = "PT XYZ", // BUKAN perusahaan
    logoUrl = null,
    location = "Kota Padang, Sumatra Barat", // BUKAN lokasi
    jobType = "Magang 6 Bulan | WFO", // BUKAN tipeKerja
    salaryRange = "Rp 2.000.000 - Rp 5.000.000", // BUKAN gaji

    // Karena di database description & requirements itu String panjang (bukan List),
    // Kita simulasikan pakai String dengan Enter (\n)
    description = "Kami mencari developer muda berbakat untuk bergabung dengan tim kami.\n\nTanggung Jawab:\n• Mengembangkan aplikasi Android.\n• Integrasi API.",
    requirements = "• Menguasai Kotlin & Jetpack Compose.\n• Paham REST API.\n• Mahasiswa tingkat akhir.",
    createdAt = "2025-01-01"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailLowonganScreen(
    lowonganId: String,
    onDaftarClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val detail = dummyDetail

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = { Text("Detail Lowongan") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            PrimaryButton(
                text = "DAFTAR SEKARANG",
                onClick = { onDaftarClick(detail.id.toString()) }, // Convert Int ke String
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 80.dp)
        ) {
            // Bagian 1: Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = detail.title, // Panggil title
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = detail.companyName, // Panggil companyName
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Info Row
                    InfoRow(icon = Icons.Default.LocationOn, text = detail.location)
                    InfoRow(icon = Icons.Default.Work, text = detail.jobType)
                    InfoRow(icon = Icons.Default.MonetizationOn, text = detail.salaryRange)
                    // (Pendidikan minimun opsional, krn gak ada di model baru, kita skip dulu)
                }
            }

            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }

            // Bagian 2: Deskripsi
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Deskripsi Pekerjaan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = detail.description ?: "Tidak ada deskripsi",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 24.sp
                    )
                }
            }

            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }

            // Bagian 3: Persyaratan (Requirements)
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Persyaratan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = detail.requirements ?: "-",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = SecondaryGreen)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}