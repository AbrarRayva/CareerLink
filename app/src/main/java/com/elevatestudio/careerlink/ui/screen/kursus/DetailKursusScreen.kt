// Lokasi: ui/screen/kursus/DetailKursusScreen.kt
package com.elevatestudio.careerlink.ui.screen.kursus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elevatestudio.careerlink.data.model.KursusDetail
import com.elevatestudio.careerlink.ui.components.ConfirmationDialog
import com.elevatestudio.careerlink.ui.components.PrimaryButton
import com.elevatestudio.careerlink.ui.screen.lowongan.SubmissionState
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen

// Data dummy
val dummyDetailKursus = KursusDetail(
    id = "1",
    penyelenggara = "UPT Kewirausahaan dan Karir Unand",
    judul = "Cara Membuat CV",
    deskripsi = "Dalam dunia kerja yang kompetitif, CV (Curriculum Vitae) adalah kunci pertama untuk membuka peluang karier. Course ini dirancang untuk membantu kamu menyusun CV yang menarik, profesional, dan sesuai standar industri. Melalui langkah-langkah praktis, kamu akan belajar bagaimana menonjolkan pengalaman, keterampilan, dan prestasi agar menarik perhatian perekrut.",
    lokasi = "Online (via Zoom)",
    tanggal = "20-21 November 2025",
    waktu = "19.00–21.00 WIB",
    level = "Pemula – Menengah",
    kapasitas = "100 peserta"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKursusScreen(
    kursusId: String,
    viewModel: KursusViewModel = viewModel(),
    onBackClick: () -> Unit,
    onDaftarSuccess: () -> Unit
) {
    val kursus = dummyDetailKursus

    val submissionState by viewModel.submissionState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDaftarDialog by remember { mutableStateOf(false) }

    LaunchedEffect(submissionState) {
        if (submissionState is SubmissionState.Error) {
            snackbarHostState.showSnackbar((submissionState as SubmissionState.Error).message)
            viewModel.resetSubmissionState()
        }
        if (submissionState is SubmissionState.Success) {
            onDaftarSuccess()
            viewModel.resetSubmissionState()
        }
    }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Daftar Kursus") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBackground,
                    navigationIconContentColor = Color.Black // Sesuaikan warna ikon
                )
            )
        },
        bottomBar = {
            PrimaryButton(
                text = "DAFTAR SEKARANG",
                onClick = { showDaftarDialog = true },
                enabled = submissionState != SubmissionState.Loading,
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
                .padding(horizontal = 16.dp)
                .padding(bottom = 80.dp) // Space buat tombol
        ) {
            // 1. Judul
            item {
                Text(kursus.penyelenggara, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                Text(
                    text = kursus.judul,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 2. Deskripsi
            item {
                Text("Deskripsi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(kursus.deskripsi, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 3. Detail
            item {
                Text("Detail", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                KursusInfoRow(icon = Icons.Default.LocationOn, text = kursus.lokasi)
                KursusInfoRow(icon = Icons.Default.CalendarToday, text = kursus.tanggal)
                KursusInfoRow(icon = Icons.Default.AccessTime, text = kursus.waktu)
                KursusInfoRow(icon = Icons.Default.BarChart, text = kursus.level)
                KursusInfoRow(icon = Icons.Default.People, text = kursus.kapasitas)
            }
        }
    }

    // Dialog Konfirmasi
    if (showDaftarDialog) {
        ConfirmationDialog(
            onDismiss = { showDaftarDialog = false },
            onConfirm = {
                showDaftarDialog = false
                viewModel.daftarKursus(kursusId)
            },
            title = "Daftar untuk kursus ini?",
            icon = { Icon(Icons.Default.Info, contentDescription = null, tint = PrimaryGreen) }
        )
    }

    // Dialog Loading
    if (submissionState == SubmissionState.Loading) {
        Dialog(onDismissRequest = {}) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun KursusInfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = SecondaryGreen, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}