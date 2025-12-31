package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.elevatestudio.careerlink.data.model.LowonganItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarLowonganScreen(
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit,
    viewModel: DaftarLowonganViewModel = viewModel()
) {
    val state = viewModel.lowonganState

    LaunchedEffect(Unit) {
        viewModel.getLowongan()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Lowongan") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Kembali") }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (state) {
                is LowonganUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is LowonganUiState.Error -> {
                    Text(state.message, modifier = Modifier.align(Alignment.Center), color = Color.Red)
                }
                is LowonganUiState.Success -> {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(state.data) { item ->
                            LowonganCard(item, onClick = { onItemClick(item.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LowonganCard(item: LowonganItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.title, style = MaterialTheme.typography.titleMedium)
            Text(item.company, style = MaterialTheme.typography.bodyMedium)
            Text(item.location, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        }
    }
}