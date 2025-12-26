package com.elevatestudio.careerlink.ui.screen.kursus

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elevatestudio.careerlink.ui.components.PrimaryButton
import com.elevatestudio.careerlink.ui.screen.lowongan.SubmissionState
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen
import com.elevatestudio.careerlink.utils.FileUtils
import com.elevatestudio.careerlink.utils.UserPreferences
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

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

    // Ambil token untuk keperluan upload & refresh
    val tokenState = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        tokenState.value = UserPreferences(context).authToken.first() ?: ""
    }

    // 1. SETUP SCANNER (Google Code Scanner)
    val scanner = remember { GmsBarcodeScanning.getClient(context) }

    // 2. SETUP UPLOAD FILE
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val file = FileUtils.getFileFromUri(context, uri)
            if (file != null) {
                // Buat Multipart Body
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                // Pastikan token ada sebelum upload
                if (tokenState.value.isNotEmpty()) {
                    viewModel.uploadBadge(tokenState.value, body)
                } else {
                    Toast.makeText(context, "Token tidak valid, silakan login ulang", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Gagal memproses file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(submissionState) {
        if (submissionState is SubmissionState.Error) {
            val msg = (submissionState as SubmissionState.Error).message
            snackbarHostState.showSnackbar(msg)
            viewModel.resetSubmissionState()
        }
    }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Scan Badge") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Kembali") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen, titleContentColor = Color.White, navigationIconContentColor = Color.White)
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
                Text("Pindai QR Code Kursus", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(32.dp))

                // BOX SCANNER (Klik untuk Scan)
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .border(2.dp, SecondaryGreen, RoundedCornerShape(16.dp))
                        .clickable {
                            // 🔥 FUNGSI SCAN 🔥
                            scanner.startScan()
                                .addOnSuccessListener { barcode ->
                                    val qrData = barcode.rawValue
                                    if (qrData != null) {
                                        scope.launch {
                                            val token = tokenState.value
                                            if (token.isNotEmpty()) {
                                                try {
                                                    // Asumsi QR isinya angka ID kursus
                                                    val courseId = qrData.toInt()
                                                    viewModel.scanQrBadge(token, courseId)
                                                } catch (e: Exception) {
                                                    Toast.makeText(context, "QR Code tidak valid: $qrData", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        }
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Gagal membuka scanner", Toast.LENGTH_SHORT).show()
                                }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.QrCodeScanner,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = SecondaryGreen
                    )
                    Text("Ketuk untuk Scan", modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp), color = Color.Gray)
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Atau unggah bukti sertifikat", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(
                    text = "Unggah File",
                    onClick = { fileLauncher.launch("image/*") } // Hanya gambar
                )
            }
        }
    }

    // Dialog Loading
    if (submissionState == SubmissionState.Loading) {
        Dialog(onDismissRequest = {}) { CircularProgressIndicator() }
    }

    // 🔥 DIALOG SUKSES KHUSUS BADGE (Ganti SuccessDialog lamaran) 🔥
    if (submissionState == SubmissionState.Success) {
        AlertDialog(
            onDismissRequest = { }, // Paksa user klik tombol
            icon = { Icon(Icons.Default.Verified, null, tint = PrimaryGreen) },
            title = { Text("Upload Berhasil!") },
            text = { Text("Sertifikat baru telah ditambahkan ke koleksi Anda.\nStatistik Anda telah diperbarui.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetSubmissionState()
                        onBackClick() // Kembali ke Dashboard -> Auto Refresh
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Lihat Koleksi Badge")
                }
            },
            containerColor = Color.White
        )
    }
}