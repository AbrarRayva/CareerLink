package com.elevatestudio.careerlink.ui.screen.kursus

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.elevatestudio.careerlink.data.model.KursusItem
import com.elevatestudio.careerlink.ui.theme.AppBackground

// Data dummy untuk course yang diambil pengguna
val dummyMyCourses = listOf(
    KursusItem("1", "UPT Unand", "Cara Membuat CV", "Selesai", "https://picsum.photos/seed/a/200"),
    KursusItem("3", "Google", "Dasar-Dasar UX", "Sedang Berlangsung", "https://picsum.photos/seed/c/200"),
    KursusItem("4", "Dicoding", "Memulai Pemrograman dengan Kotlin", "Selesai", "https://picsum.photos/seed/d/200")
)

// Data class dan data dummy untuk badge
data class BadgeItem(
    val id: String,
    val courseTitle: String,
    val imageVector: ImageVector
)

val dummyMyBadges = listOf(
    BadgeItem("1", "Cara Membuat CV", Icons.Default.WorkspacePremium),
    BadgeItem("4", "Memulai Pemrograman dengan Kotlin", Icons.Default.WorkspacePremium)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCoursesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetailKursus: (String) -> Unit,
    onNavigateToBadgeScan: () -> Unit
) {
    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = { Text("Course & Badge Saya") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBackground
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            // 1. Bagian Statistik
            item {
                Text(
                    text = "Statistik",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Total Course Diambil: ${dummyMyCourses.size}")
                        Text("Course Selesai: ${dummyMyCourses.count { it.tipe == "Selesai" }}")
                        Text("Course Aktif: ${dummyMyCourses.count { it.tipe != "Selesai" }}")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 2. Bagian Badge Saya
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Badge Saya",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    FilledTonalButton(onClick = onNavigateToBadgeScan) {
                        Icon(
                            Icons.Default.QrCodeScanner,
                            contentDescription = "Scan Badge",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Tambahkan Badge")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (dummyMyBadges.isEmpty()) {
                    Text(
                        text = "Anda belum memiliki badge. Selesaikan course untuk mendapatkannya!",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(dummyMyBadges) { badge ->
                            BadgeItemCard(badge = badge)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 3. Bagian Daftar Course Saya
            item {
                Text(
                    text = "Daftar Course Saya",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp)
                )
            }

            items(dummyMyCourses) { kursus ->
                KursusItemCard(
                    kursus = kursus,
                    onClick = { onNavigateToDetailKursus(kursus.id) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun BadgeItemCard(badge: BadgeItem, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = badge.imageVector,
            contentDescription = "Badge untuk ${badge.courseTitle}",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = badge.courseTitle,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun KursusItemCard(kursus: KursusItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = kursus.judul, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Oleh: ${kursus.penyelenggara}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Status: ${kursus.tipe}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
