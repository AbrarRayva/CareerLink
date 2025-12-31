package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.elevatestudio.careerlink.data.model.LowonganDetail
import com.elevatestudio.careerlink.ui.components.PrimaryButton
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailLowonganScreen(
    lowonganId: String,
    onBackClick: () -> Unit,
    onAjukanClick: (String) -> Unit,
    viewModel: DaftarLowonganViewModel = viewModel()
) {
    val uiState = viewModel.detailState

    LaunchedEffect(lowonganId) {
        viewModel.getDetailLowongan(lowonganId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Lowongan") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Kembali") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (uiState) {
                is DetailLowonganUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PrimaryGreen)
                }
                is DetailLowonganUiState.Error -> {
                    Text(uiState.message, modifier = Modifier.align(Alignment.Center), color = Color.Red)
                }
                is DetailLowonganUiState.Success -> {
                    val data = uiState.data
                   
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Text(text = data.title, style = MaterialTheme.typography.headlineSmall)
                        Text(text = data.company, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Lokasi: ${data.location}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Gaji: ${data.salary ?: "Dirahasiakan"}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Deskripsi", style = MaterialTheme.typography.titleMedium)
                        Text(text = data.description, style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { onAjukanClick(data.id) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                        ) {
                            Text("Ajukan Lamaran", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}