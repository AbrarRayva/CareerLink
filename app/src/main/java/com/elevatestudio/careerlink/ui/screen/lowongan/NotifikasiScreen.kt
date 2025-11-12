package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.elevatestudio.careerlink.data.model.NotifikasiItem
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen

// Data dummy (nanti dapet dari ViewModel)
val dummyNotif = listOf(
    NotifikasiItem("1", "Lowongan dan Magang", "PT XYZ membuka magang khusus...", "10:00"),
    NotifikasiItem("2", "Lowongan dan Magang", "Part-time khusus buat kamu...", "09:30")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifikasiScreen(
    // viewModel: LowonganViewModel,
    onBackClick: () -> Unit,
    onLihatClick: (String) -> Unit
) {
    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = { Text("Notifikasi") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dummyNotif) { notif ->
                NotifikasiCard(
                    item = notif,
                    onLihat = { onLihatClick(notif.id) },
                    onNonaktifkan = {
                        // Nanti panggil viewModel.hapusNotifikasi(notif.id)
                    }
                )
            }
        }
    }
}

@Composable
fun NotifikasiCard(
    item: NotifikasiItem,
    onLihat: () -> Unit,
    onNonaktifkan: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.modul, // Judul Modul
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.ringkasan, // Ringkasan
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onNonaktifkan) {
                    Text("Nonaktifkan")
                }
                TextButton(onClick = onLihat) {
                    Text("Lihat")
                }
            }
        }
    }
}