// Lokasi: ui/screen/lowongan/DetailLowonganScreen.kt
package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
// --- INI IMPORT YANG BARU DITAMBAH ---
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
// --- SELESAI IMPORT IKON ---
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
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen // <-- IMPORT TAMBAHAN
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen

// Data dummy (nanti ini dapet dari ViewModel)
val dummyDetail = LowonganDetail(
    id = "1",
    posisi = "Software Developer Intern",
    perusahaan = "PT XYZ",
    lokasi = "Kota Padang, Sumatra Barat",
    pendidikanMin = "Minimum S1 | Semester 7",
    tipeKerja = "Magang 6 Bulan | WFO",
    gaji = "Rp 2.000.000 - Rp 5.000.000",
    deskripsi = "Deskripsi detail ada di sini...",
    responsibilities = listOf(
        "Develop and maintain Windows, Web, and Mobile applications.",
        "Design and integrate RESTful / GraphQL APIs."
    ),
    coreSkills = listOf(
        "Strong programming experience in C# / .NET",
        "Proficiency in Laravel (PHP)"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailLowonganScreen(
    lowonganId: String,
    // viewModel: LowonganViewModel, // Nanti di-inject
    onDaftarClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    // Nanti panggil viewModel.getDetailLowongan(lowonganId)
    val detail = dummyDetail // Pakai data dummy dulu

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
                    containerColor = PrimaryGreen, // <-- Error kalo PrimaryGreen gak di-import
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // Tombol "DAFTAR" nempel di bawah
            PrimaryButton(
                text = "DAFTAR",
                onClick = { onDaftarClick(detail.id) },
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
                .padding(bottom = 80.dp) // Kasih space buat tombol daftar
        ) {
            // Bagian 1: Ringkasan
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = detail.posisi,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = detail.perusahaan,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoRow(icon = Icons.Default.LocationOn, text = detail.lokasi)
                    InfoRow(icon = Icons.Default.School, text = detail.pendidikanMin)
                    InfoRow(icon = Icons.Default.Work, text = detail.tipeKerja)
                    InfoRow(icon = Icons.Default.MonetizationOn, text = detail.gaji)
                }
            }

            item { Divider() }

            // Bagian 2: Deskripsi Detail
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Deskripsi Detail",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "1. Responsibilities",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    // Pake items biar list-nya dinamis
                }
            }
            items(detail.responsibilities) { item ->
                Text("• $item", modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            }

            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. Core Skills",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            items(detail.coreSkills) { item ->
                Text("• $item", modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
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