// Lokasi: ui/screen/lowongan/DaftarLowonganScreen.kt
package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.elevatestudio.careerlink.data.model.LowonganItem
import com.elevatestudio.careerlink.ui.components.AppBottomNavBar
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen

// Filter Chips (Sementara statis dulu, nanti bisa disambung ke API)
val filterChips = listOf("Full Time", "Part Time", "Internship", "Freelance")

// Konstanta Base URL (Sesuaikan dengan IP Laptop kamu yg di RetrofitClient)
// Gunanya buat nampilin gambar logo
const val BASE_IMAGE_URL = "http://192.168.18.27:3000/api/" // <-- PASTIIN SAMA KAYAK RETROFIT CLIENT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarLowonganScreen(
    viewModel: DaftarLowonganViewModel = viewModel(), // <-- Sudah di-inject
    onLowonganClick: (String) -> Unit,
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    var selectedChip by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showFilterSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = AppBackground,
        bottomBar = {
            AppBottomNavBar(
                currentRoute = "lowongan",
                onItemSelected = onNavigate
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- HEADER & SEARCH ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        // Panggil API search realtime saat ngetik (atau bisa pake tombol enter)
                        viewModel.getAllLowongan(it)
                    },
                    placeholder = { Text("Cari posisi atau perusahaan...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Filter Button Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilterButton { showFilterSheet = true }
                    Spacer(modifier = Modifier.width(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filterChips) { chipLabel ->
                            FilterChip(
                                selected = selectedChip == chipLabel,
                                onClick = {
                                    selectedChip = if (selectedChip == chipLabel) null else chipLabel
                                    // Nanti disini panggil viewModel.filterBy(selectedChip)
                                },
                                label = { Text(chipLabel) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // --- DAFTAR LOWONGAN (REAL DATA) ---
            when (val state = uiState) {
                is LowonganUiState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = PrimaryGreen)
                        }
                    }
                }
                is LowonganUiState.Error -> {
                    item {
                        Text(
                            text = "Error: ${state.message}",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = { viewModel.getAllLowongan() }) {
                            Text("Coba Lagi")
                        }
                    }
                }
                is LowonganUiState.Success -> {
                    if (state.lowongan.isEmpty()) {
                        item {
                            Text(
                                text = "Tidak ada lowongan ditemukan.",
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                color = Color.Gray
                            )
                        }
                    } else {
                        items(state.lowongan) { lowongan ->
                            LowonganCard(
                                item = lowongan,
                                onClick = {
                                    // Pastikan konversi ID ke String
                                    onLowonganClick(lowongan.id.toString())
                                }
                            )
                        }
                    }
                }
            }
            // Spacer bawah biar gak ketutup navbar
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    // Sheet Filter (UI Only untuk sekarang)
    if (showFilterSheet) {
        // ... (Kode FilterLowonganSheet kamu biarkan saja atau buat dummy composable di bawah)
    }
}

// --- CARD LOWONGAN YANG SUDAH DIUPDATE ---
@Composable
fun LowonganCard(
    item: LowonganItem,
    onClick: () -> Unit
) {
    // Gabungkan Base URL + Path dari database untuk gambar
    // Misal DB: uploads/logo.png -> Coil butuh: http://ip:3000/uploads/logo.png
    val fullLogoUrl = if (item.logoUrl != null && !item.logoUrl.startsWith("http")) {
        "$BASE_IMAGE_URL${item.logoUrl}"
    } else {
        item.logoUrl
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // KIRI: Logo Perusahaan
            AsyncImage(
                model = fullLogoUrl,
                contentDescription = "Logo ${item.companyName}",
                fallback = ColorPainter(Color.LightGray), // Warna abu kalo gak ada logo
                error = ColorPainter(Color.LightGray),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // TENGAH: Info Lowongan
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title, // Judul Posisi
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    maxLines = 1
                )
                Text(
                    text = item.companyName, // Nama Perusahaan
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Info Tambahan (Lokasi & Tipe)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, Modifier.size(12.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(item.location, fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(Icons.Default.Work, null, Modifier.size(12.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(item.jobType, fontSize = 12.sp, color = Color.Gray)
                }
            }

            // KANAN: Gaji (Lebih penting daripada gambar acak)
            // Atau kalau mau tetap gambar, backend harus siapin gambar cover.
            // Tapi biasanya Gaji lebih menarik buat pelamar.
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Gaji",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
                Text(
                    text = item.salaryRange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = PrimaryGreen
                )
            }
        }
    }
}

// Komponen Pembantu (Tetap)
@Composable
fun FilterButton(onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text("Filter") },
        leadingIcon = { Icon(Icons.Default.FilterList, contentDescription = "Filter") },
    )
}
