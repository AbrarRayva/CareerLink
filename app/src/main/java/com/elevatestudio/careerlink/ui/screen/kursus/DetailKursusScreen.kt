package com.elevatestudio.careerlink.ui.screen.kursus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.elevatestudio.careerlink.data.model.KursusDetail
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.ui.components.ConfirmationDialog
import com.elevatestudio.careerlink.ui.components.PrimaryButton
import com.elevatestudio.careerlink.ui.screen.lowongan.SubmissionState
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen
import com.elevatestudio.careerlink.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKursusScreen(
    kursusId: String,
    viewModel: KursusViewModel = viewModel(),
    onBackClick: () -> Unit,
    onDaftarSuccess: () -> Unit
) {
    val context = LocalContext.current
    val detailState by viewModel.detailState.collectAsState()
    val submissionState by viewModel.submissionState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showDaftarDialog by remember { mutableStateOf(false) }
    var userToken by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            val prefs = UserPreferences(context)
            userToken = prefs.authToken.first() ?: ""
        }
    }

    LaunchedEffect(kursusId) {
        viewModel.getDetailKursus(kursusId)
    }

    LaunchedEffect(submissionState) {
        if (submissionState is SubmissionState.Error) {
            val msg = (submissionState as SubmissionState.Error).message
            // 🔥 HANDLING ERROR LEBIH PINTAR 🔥
            if (msg.contains("sudah terdaftar", ignoreCase = true)) {
                // Kalau udah daftar, tampilkan pesan sukses aja atau info
                snackbarHostState.showSnackbar("Anda sudah terdaftar di kursus ini ✅")
            } else {
                snackbarHostState.showSnackbar(msg)
            }
            viewModel.resetSubmissionState()
        }
        if (submissionState is SubmissionState.Success) {
            // Tampilkan Dialog Sukses
            // Dialog ini nanti memicu onDaftarSuccess -> Pindah halaman
            onDaftarSuccess()
            viewModel.resetSubmissionState()
        }
    }

    Scaffold(
        containerColor = AppBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Detail Kursus") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBackground,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            if (detailState is KursusDetailUiState.Success) {
                PrimaryButton(
                    text = "DAFTAR SEKARANG",
                    onClick = { showDaftarDialog = true },
                    enabled = submissionState != SubmissionState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (detailState) {
                is KursusDetailUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PrimaryGreen
                    )
                }
                is KursusDetailUiState.Error -> {
                    Text(
                        text = (detailState as KursusDetailUiState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is KursusDetailUiState.Success -> {
                    val kursus = (detailState as KursusDetailUiState.Success).data
                    KursusContent(kursus)
                }
            }
        }
    }

    if (showDaftarDialog) {
        ConfirmationDialog(
            onDismiss = { showDaftarDialog = false },
            onConfirm = {
                showDaftarDialog = false
                if (userToken.isNotEmpty()) {
                    viewModel.daftarKursus(kursusId, userToken)
                }
            },
            title = "Daftar untuk kursus ini?",
            icon = { Icon(Icons.Default.Info, contentDescription = null, tint = PrimaryGreen) }
        )
    }

    if (submissionState == SubmissionState.Loading) {
        Dialog(onDismissRequest = {}) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
    }
}

@Composable
fun KursusContent(kursus: KursusDetail) {
    val fullImageUrl = if (kursus.imageUrl != null && !kursus.imageUrl.startsWith("http")) {
        "${ApiClient.BASE_URL}${kursus.imageUrl}"
    } else {
        kursus.imageUrl
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = 80.dp)
    ) {
        item {
            if (fullImageUrl != null) {
                AsyncImage(
                    model = fullImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item {
            Text(kursus.providerName, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
            Text(
                text = kursus.title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text("Deskripsi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(kursus.description ?: "-", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text("Detail", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            val lokasiDisplay = if (kursus.locationDetail != null) {
                "${kursus.locationType} (${kursus.locationDetail})"
            } else {
                kursus.locationType
            }

            KursusInfoRow(icon = Icons.Default.LocationOn, text = lokasiDisplay)

            if (kursus.dateStart != null) {
                KursusInfoRow(icon = Icons.Default.CalendarToday, text = kursus.dateStart.take(10))
            }

            if (kursus.quota != null) {
                KursusInfoRow(icon = Icons.Default.People, text = "${kursus.quota} Peserta")
            }
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