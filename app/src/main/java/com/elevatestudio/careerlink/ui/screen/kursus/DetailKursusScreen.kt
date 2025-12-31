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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elevatestudio.careerlink.data.model.Course
import com.elevatestudio.careerlink.ui.components.ConfirmationDialog
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
    val uiState by viewModel.detailUiState.collectAsState()
    val submissionState by viewModel.submissionState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(kursusId) {
        val idInt = kursusId.toIntOrNull() ?: 0
        viewModel.getDetailKursus(idInt)
    }

    LaunchedEffect(submissionState) {
        if (submissionState is SubmissionState.Success) {
            onDaftarSuccess()
            viewModel.resetSubmissionState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Kursus") },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
            )
        },
        bottomBar = {
            if (uiState is KursusDetailUiState.Success) {
               
                Button(
                    onClick = { showDialog = true },
                    enabled = submissionState !is SubmissionState.Loading,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text(
                        text = if (submissionState is SubmissionState.Loading) "MEMPROSES..." else "DAFTAR SEKARANG",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when (val state = uiState) {
                is KursusDetailUiState.Success -> KursusContent(state.data)
                is KursusDetailUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = PrimaryGreen)
                is KursusDetailUiState.Error -> Text(state.message, Modifier.align(Alignment.Center), color = Color.Red)
            }
        }
    }

    if (showDialog) {
        ConfirmationDialog(
            onDismiss = { showDialog = false },
            onConfirm = {
                showDialog = false
                val idInt = kursusId.toIntOrNull() ?: 0
                viewModel.enrollCourse(idInt)
            },
            title = "Daftar Kursus?",
            icon = { Icon(Icons.Default.School, null, tint = PrimaryGreen) }
        )
    }
}

@Composable
fun KursusContent(kursus: Course) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        item {
            Box(Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp)).background(PrimaryGreen.copy(0.2f)), Alignment.Center) {
                Icon(Icons.Default.School, null, tint = PrimaryGreen, modifier = Modifier.size(80.dp))
            }
            Spacer(Modifier.height(16.dp))
        }
        item {
            Text(kursus.title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Text("Deskripsi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(kursus.description ?: "-", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            Text("Detail", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            KursusInfoRow(Icons.Default.Person, "Mentor: ${kursus.mentor ?: "-"}")
            KursusInfoRow(Icons.Default.AttachMoney, "Harga: ${kursus.price ?: "Gratis"}")
        }
    }
}

@Composable
fun KursusInfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, null, tint = SecondaryGreen, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}