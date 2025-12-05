// Lokasi: ui/screen/lowongan/NotifikasiScreen.kt
package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elevatestudio.careerlink.data.model.NotifikasiItem
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen

// Data Dummy
val dummyNotifikasi = listOf(
    NotifikasiItem(
        id = 1,
        title = "Lamaran Diterima",
        message = "Selamat! Lamaranmu di PT XYZ masuk tahap interview.",
        date = "2 jam lalu",
        isRead = false
    ),
    NotifikasiItem(
        id = 2,
        title = "Info Kursus Baru",
        message = "Kursus UI/UX Design baru saja rilis. Cek sekarang!",
        date = "1 hari lalu",
        isRead = true
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifikasiScreen(
    onBackClick: () -> Unit,
    onLihatClick: (Int) -> Unit // <-- TAMBAHAN: Biar AppNavigation tidak error
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
        ) {
            items(dummyNotifikasi) { notif ->
                NotifikasiItemCard(
                    item = notif,
                    onClick = { onLihatClick(notif.id) } // <-- Aksi Klik dipanggil disini
                )
            }
        }
    }
}

@Composable
fun NotifikasiItemCard(
    item: NotifikasiItem,
    onClick: () -> Unit // <-- TAMBAHAN: Parameter klik
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() }, // <-- Bikin card bisa diklik
        colors = CardDefaults.cardColors(containerColor = if (item.isRead) Color.White else Color(0xFFE8F5E9)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Default.Notifications,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}