// Lokasi: ui/screen/kursus/DashboardKursusScreen.kt
package com.elevatestudio.careerlink.ui.screen.kursus

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elevatestudio.careerlink.data.model.BadgeItem
import com.elevatestudio.careerlink.data.model.KursusItem
import com.elevatestudio.careerlink.ui.components.AppBottomNavBar
import com.elevatestudio.careerlink.ui.theme.AppBackground

// Data dummy
val dummyBadges = listOf(
    BadgeItem("1", "", "Badge 1"),
    BadgeItem("2", "", "Badge 2"),
    BadgeItem("3", "", "Badge 3"),
    BadgeItem("4", "", "Badge 4")
)
val dummyRekomendasi = listOf(
    KursusItem("1", "UPT Unand", "Cara Membuat CV", "Offline", "https://picsum.photos/seed/a/200"),
    KursusItem("2", "FTI Unand", "Pintar UI/UX", "Online", "https://picsum.photos/seed/b/200")
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DashboardKursusScreen(
    onNavigate: (String) -> Unit,
    onNavigateToDaftarKursus: () -> Unit,
    onNavigateToDetailKursus: (String) -> Unit,
    onNavigateToBadgeScan: () -> Unit
) {
    val username = "User" // Data dummy

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    Scaffold(
        containerColor = AppBackground,
        bottomBar = {
            AppBottomNavBar(
                currentRoute = "kursus",
                onItemSelected = onNavigate
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 1. Search Bar
            item {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    readOnly = true,
                    placeholder = { Text("Search") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToDaftarKursus() }
                )
            }

            // 2. Halo, Username
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Halo, $username",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Selamat pagi!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }

            // 3. Course dan Badge Saya (Dengan Animasi)
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500, delayMillis = 200)) + slideInVertically { it / 2 }
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Course dan Badge Saya →",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateToBadgeScan() }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(dummyBadges) { badge ->
                                AsyncImage(
                                    model = badge.imageUrl,
                                    contentDescription = badge.title,
                                    placeholder = ColorPainter(Color.LightGray),
                                    fallback = ColorPainter(Color.Gray),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                )
                            }
                        }
                    }
                }
            }

            // 4. Statistik course (Dengan Animasi)
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500, delayMillis = 400)) + slideInVertically { it / 2 }
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Statistik course →",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { /* TODO: Navigasi ke statistik */ }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        AsyncImage(
                            model = "", // URL gambar statistik dari ViewModel
                            contentDescription = "Statistik",
                            placeholder = ColorPainter(Color.LightGray),
                            fallback = ColorPainter(Color.Gray),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
            }

            // 5. Course rekomendasi (Dengan Animasi)
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500, delayMillis = 600)) + slideInVertically { it / 2 }
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Course rekomendasi →",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onNavigateToDaftarKursus() }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(dummyRekomendasi) { kursus ->
                                KursusRekomendasiCard(
                                    item = kursus,
                                    onClick = { onNavigateToDetailKursus(kursus.id) }
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun KursusRekomendasiCard(item: KursusItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(width = 180.dp, height = 120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.judul,
            placeholder = ColorPainter(Color.LightGray),
            fallback = ColorPainter(Color.Gray),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}