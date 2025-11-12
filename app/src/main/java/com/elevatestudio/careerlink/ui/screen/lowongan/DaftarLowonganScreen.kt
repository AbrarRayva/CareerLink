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
import androidx.compose.material.icons.filled.Assignment // Hapus ini kalo gak dipake lagi
import androidx.compose.material.icons.filled.Book // Hapus ini kalo gak dipake lagi
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
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
import coil.compose.AsyncImage
import com.elevatestudio.careerlink.data.model.LowonganItem
import com.elevatestudio.careerlink.ui.components.AppBottomNavBar
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen

// --- INI BAGIAN YANG DIPERBAIKI (Complaint 3) ---
// Data dummy (nanti ini dapet dari ViewModel)
val dummyList = listOf(
    LowonganItem("1", "Software Developer Intern", "PT XYZ", "", "https://picsum.photos/seed/1/200"),
    LowonganItem("2", "UI/UX Designer Part-Time", "PT ABCDE", "", "https://picsum.photos/seed/2/200"),
    LowonganItem("3", "System Analyst Intern", "Dash Service Inc.", "", "https://picsum.photos/seed/3/200")
)
val filterChips = listOf("Magang", "Part-Time", "Freelance")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarLowonganScreen(
    // viewModel: LowonganViewModel, // Nanti di-inject
    onLowonganClick: (String) -> Unit, // Navigasi ke detail
    onNavigate: (String) -> Unit // Navigasi via navbar
) {
    // (Isi fungsi ini tetap sama, tidak perlu diubah)

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
            // Bagian Header (tetap sama)
            item {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari lowongan...") },
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilterButton { showFilterSheet = true }
                    Spacer(modifier = Modifier.width(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filterChips) { chipLabel ->
                            FilterChip(
                                selected = selectedChip == chipLabel,
                                onClick = {
                                    selectedChip = if (selectedChip == chipLabel) null else chipLabel
                                },
                                label = { Text(chipLabel) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Daftar Lowongan
            items(dummyList) { lowongan ->
                LowonganCard( // <-- Card ini akan memanggil versi baru di bawah
                    item = lowongan,
                    onClick = { onLowonganClick(lowongan.id) }
                )
            }
        }
    }

    if (showFilterSheet) {
        FilterLowonganSheet(
            sheetState = sheetState,
            onDismiss = { showFilterSheet = false },
            onApply = {
                showFilterSheet = false
            }
        )
    }
}

// (FilterButton Composable tetap sama)
@Composable
fun FilterButton(onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text("Filter") },
        leadingIcon = { Icon(Icons.Default.FilterList, contentDescription = "Filter") },
    )
}

// --- INI FUNGSI YANG DIPERBAIKI (Complaint 3) ---
@Composable
fun LowonganCard(
    item: LowonganItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        // Ganti warna card biar lebih pas sama desain
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // KIRI: Logo Perusahaan (Lingkaran)
            AsyncImage(
                model = item.logoUrl,
                contentDescription = "Logo ${item.perusahaan}",
                fallback = ColorPainter(Color.Gray),
                placeholder = ColorPainter(Color.LightGray),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // TENGAH: Info Teks
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.posisi,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black // Ganti warna biar kebaca
                )
                Text(
                    text = item.perusahaan,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // KANAN: Gambar Lowongan (Persegi)
            AsyncImage(
                model = item.imageUrl, // <-- Panggil data gambar baru
                contentDescription = "Gambar ${item.posisi}",
                fallback = ColorPainter(Color.Gray),
                placeholder = ColorPainter(Color.LightGray),
                contentScale = ContentScale.Crop, // Biar pas
                modifier = Modifier
                    .size(width = 64.dp, height = 64.dp) // Ukuran persegi
                    .clip(RoundedCornerShape(8.dp)) // Bikin sudutnya tumpul
            )
        }
    }
}