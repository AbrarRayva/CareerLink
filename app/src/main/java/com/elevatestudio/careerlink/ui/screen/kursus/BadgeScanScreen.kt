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
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen
import com.elevatestudio.careerlink.utils.FileUtils
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
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
    val submissionState by viewModel.submissionState.collectAsState()
    val scanner = remember { GmsBarcodeScanning.getClient(context) }

   
    val fileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            val file = FileUtils.getFileFromUri(context, uri)
            if (file != null) {
               
                val mimeType = when {
                    file.name.endsWith(".png", true) -> "image/png"
                    file.name.endsWith(".pdf", true) -> "application/pdf"
                    else -> "image/jpeg"
                }

                val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())

               
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                viewModel.uploadBadge(body)
            } else {
                Toast.makeText(context, "Gagal membaca file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(submissionState) {
        if (submissionState is SubmissionState.Error) {
            val msg = (submissionState as SubmissionState.Error).message
           
            if (msg.contains("<!DOCTYPE html>", true)) {
                Toast.makeText(context, "Gagal: Format file ditolak server. Pastikan JPG/PNG/PDF.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
            viewModel.resetSubmissionState()
        }
    }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = { Text("Scan Badge") },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen, titleContentColor = Color.White, navigationIconContentColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Pindai QR Code", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier.size(250.dp).border(2.dp, SecondaryGreen, RoundedCornerShape(16.dp))
                        .clickable {
                            scanner.startScan().addOnSuccessListener { barcode ->
                                val code = barcode.rawValue?.toIntOrNull()
                                if (code != null) viewModel.scanQrBadge(code)
                                else Toast.makeText(context, "QR Tidak Valid", Toast.LENGTH_SHORT).show()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.QrCodeScanner, null, Modifier.size(100.dp), tint = SecondaryGreen)
                    Text("Ketuk untuk Scan", Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp), color = Color.Gray)
                }
            }

            Column {
                Text("Atau unggah sertifikat (PDF/JPG)", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                   
                    onClick = { fileLauncher.launch("*/*") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Unggah File", color = Color.White)
                }
            }
        }
    }

    if (submissionState is SubmissionState.Success) {
        AlertDialog(
            onDismissRequest = {},
            icon = { Icon(Icons.Default.Verified, null, tint = PrimaryGreen) },
            title = { Text("Berhasil!") },
            text = { Text("Badge berhasil ditambahkan.") },
            confirmButton = {
                Button(onClick = {
                    viewModel.resetSubmissionState()
                    onBackClick()
                }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)) { Text("OK") }
            }
        )
    }

    if (submissionState == SubmissionState.Loading) {
        Dialog(onDismissRequest = {}) { CircularProgressIndicator(color = PrimaryGreen) }
    }
}