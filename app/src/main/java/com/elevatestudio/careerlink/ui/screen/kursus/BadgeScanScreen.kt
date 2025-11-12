// Lokasi: ui/screen/kursus/BadgeScanScreen.kt
package com.elevatestudio.careerlink.ui.screen.kursus

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elevatestudio.careerlink.ui.components.PrimaryButton
import com.elevatestudio.careerlink.ui.components.SuccessDialog
import com.elevatestudio.careerlink.ui.screen.lowongan.SubmissionState
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgeScanScreen(
    viewModel: KursusViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val submissionState by viewModel.submissionState.collectAsState()

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            Toast.makeText(context, "Fitur upload belum siap", Toast.LENGTH_SHORT).show()
            // TODO: Konversi URI ke MultipartBody.Part dan panggil viewModel.uploadSertifikat(file)
        }
    }

    // --- ANIMASI UNTUK IKON QR ---
    val infiniteTransition = rememberInfiniteTransition(label = "qr-pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "qr-alpha"
    )
    // --- SELESAI ANIMASI ---

    LaunchedEffect(submissionState) {
        if (submissionState is SubmissionState.Error) {
            snackbarHostState.showSnackbar((submissionState as SubmissionState.Error).message)
            viewModel.resetSubmissionState()
        }
    }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Course Badge Scan") },
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // 1. Teks
                Text(
                    text = "Pindai QR Code Sertifikat Anda di sini",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))

                // 2. Box QR
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .aspectRatio(1f)
                        .border(2.dp, SecondaryGreen, RoundedCornerShape(16.dp))
                        .clickable {
                            Toast.makeText(context, "Fitur Scan QR belum siap", Toast.LENGTH_SHORT).show()
                            // TODO: Panggil qrLauncher.launch()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.QrCodeScanner,
                        contentDescription = "Scan QR",
                        modifier = Modifier
                            .size(100.dp)
                            .alpha(alpha), // Terapkan animasi alpha
                        tint = SecondaryGreen
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "atau unggah file sertifikat",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }

            // 3. Tombol
            Column(modifier = Modifier.fillMaxWidth()) {
                PrimaryButton(
                    text = "Unggah File",
                    onClick = {
                        fileLauncher.launch("*/*") // Bisa pilih PDF, JPG, PNG
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryGreen.copy(alpha = 0.6f),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Kembali")
                }
            }
        }
    }

    // Dialog Loading
    if (submissionState == SubmissionState.Loading) {
        Dialog(onDismissRequest = {}) {
            CircularProgressIndicator()
        }
    }

    // Dialog Sukses (pop-up)
    if (submissionState == SubmissionState.Success) {
        // Kita PAKE ULANG SuccessDialog dari modul lowongan
        SuccessDialog(
            onDismiss = { viewModel.resetSubmissionState() },
            onGoToHome = {
                viewModel.resetSubmissionState()
                onBackClick() // Cukup kembali ke layar sebelumnya
            }
        )
    }
}