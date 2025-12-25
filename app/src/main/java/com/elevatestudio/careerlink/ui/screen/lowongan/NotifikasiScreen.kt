package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elevatestudio.careerlink.data.model.NotifikasiItem
import com.elevatestudio.careerlink.data.remote.RetrofitClient
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifikasiScreen(
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var listNotif by remember { mutableStateOf<List<NotifikasiItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    fun loadNotifikasi() {
        scope.launch {
            try {
                // Notifikasi biasanya tidak butuh token kalau public,
                // tapi kalau private, tambahkan header token di ApiService
                val response = RetrofitClient.instance.getNotifikasi()
                if (response.isSuccessful) {
                    listNotif = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                // Error handling
            } finally {
                isLoading = false
            }
        }
    }

    fun hapusNotifikasi(id: String) {
        scope.launch {
            try {
                val response = RetrofitClient.instance.hapusNotifikasi(id)
                if (response.isSuccessful) {
                    // Refresh list setelah hapus
                    listNotif = listNotif.filter { it.id.toString() != id }
                }
            } catch (e: Exception) {
                // Error handling
            }
        }
    }

    LaunchedEffect(Unit) {
        loadNotifikasi()
    }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = { Text("Notifikasi", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else if (listNotif.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Notifications, null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(8.dp))
                    Text("Belum ada notifikasi.", color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(listNotif) { item ->
                    Card(
                        onClick = {
                            if (item.jobId != null) {
                                onItemClick(item.jobId.toString())
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.message,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = item.date, // Pastikan backend kirim tanggal
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }

                            IconButton(onClick = { hapusNotifikasi(item.id.toString()) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}