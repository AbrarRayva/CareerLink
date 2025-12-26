package com.elevatestudio.careerlink.ui.screen.kursus

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.elevatestudio.careerlink.data.model.BadgeItem
import com.elevatestudio.careerlink.data.model.KursusItem
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.ui.components.AppBottomNavBar
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DashboardKursusScreen(
    onNavigate: (String) -> Unit,
    onNavigateToDaftarKursus: () -> Unit,
    onNavigateToDetailKursus: (String) -> Unit,
    onNavigateToBadgeScan: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: KursusViewModel = viewModel()

    // States
    val rekomendasiState by viewModel.rekomendasiState.collectAsState()
    val badgeState by viewModel.badgeState.collectAsState()
    val statsState by viewModel.statsState.collectAsState()

    val scope = rememberCoroutineScope()
    var username by remember { mutableStateOf("Sobat Career") }
    var isVisible by remember { mutableStateOf(false) }

    // Load Data
    LaunchedEffect(Unit) {
        isVisible = true
        scope.launch {
            val prefs = UserPreferences(context)
            val token = prefs.authToken.first()
            val name = prefs.userName.first()

            if (!name.isNullOrEmpty()) username = name

            if (token != null) {
                viewModel.refreshDashboard(token)
            } else {
                viewModel.getRecommendedCourses()
            }
        }
    }

    Scaffold(
        containerColor = AppBackground,
        bottomBar = {
            AppBottomNavBar(currentRoute = "kursus", onItemSelected = onNavigate)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // 1. Header (Halo User)
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Halo, $username \uD83D\uDC4B",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tingkatkan skillmu hari ini!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }

            // 2. Badge Saya
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500)) + slideInVertically { it / 2 }
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Koleksi Badge Saya", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        val badges = if (badgeState is BadgeUiState.Success) (badgeState as BadgeUiState.Success).data else emptyList()

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(badges) { badge ->
                                BadgeCircleItem(badge)
                            }
                            item {
                                AddBadgeButton(onClick = onNavigateToBadgeScan)
                            }
                        }
                    }
                }
            }

            // 3. Statistik Course (Fixed Syntax Error)
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(600)) + slideInVertically { it / 2 }
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Statistik Belajar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                StatItem(
                                    value = statsState.activeCourses.toString(),
                                    label = "Aktif",
                                    icon = Icons.Default.School,
                                    color = Color(0xFF2196F3),
                                    onClick = { onNavigate("status_active") }
                                )
                                StatItem(
                                    value = statsState.completedCourses.toString(),
                                    label = "Selesai",
                                    icon = Icons.Default.Verified,
                                    color = PrimaryGreen,
                                    onClick = { onNavigate("status_completed") }
                                )
                                StatItem(
                                    value = statsState.totalBadges.toString(),
                                    label = "Badge",
                                    icon = Icons.Default.EmojiEvents,
                                    color = Color(0xFFFFC107),
                                    onClick = { onNavigateToBadgeScan() }
                                )
                            }
                        }
                    }
                }
            }

            // 4. Course Rekomendasi
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(700)) + slideInVertically { it / 2 }
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Rekomendasi Untukmu", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            TextButton(onClick = onNavigateToDaftarKursus) {
                                Text("Lihat Semua", color = PrimaryGreen)
                            }
                        }

                        when (rekomendasiState) {
                            is KursusUiState.Loading -> Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) }
                            is KursusUiState.Success -> {
                                val list = (rekomendasiState as KursusUiState.Success).data
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    items(list) { kursus ->
                                        KursusRekomendasiCard(item = kursus, onClick = { onNavigateToDetailKursus(kursus.id.toString()) })
                                    }
                                }
                            }
                            else -> Text("Gagal memuat rekomendasi", color = Color.Red)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}

// --- KOMPONEN PENDUKUNG ---

@Composable
fun BadgeCircleItem(item: BadgeItem) {
    val fullUrl = if (item.imageUrl != null && !item.imageUrl.startsWith("http")) {
        "${ApiClient.BASE_URL}${item.imageUrl}"
    } else item.imageUrl

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(70.dp)) {
        AsyncImage(
            model = fullUrl,
            contentDescription = item.title,
            placeholder = ColorPainter(Color.LightGray),
            error = ColorPainter(Color.Gray),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, PrimaryGreen, CircleShape)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(item.title, style = MaterialTheme.typography.labelSmall, maxLines = 1, fontSize = 10.sp)
    }
}

@Composable
fun AddBadgeButton(onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(70.dp)) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9))
                .clickable { onClick() }
                .border(1.dp, PrimaryGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = "Tambah", tint = PrimaryGreen, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text("Tambah", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
    }
}

@Composable
fun StatItem(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

@Composable
fun KursusRekomendasiCard(item: KursusItem, onClick: () -> Unit) {
    val fullUrl = if (item.imageUrl != null && !item.imageUrl.startsWith("http")) {
        "${ApiClient.BASE_URL}${item.imageUrl}"
    } else {
        item.imageUrl
    }

    Card(
        modifier = Modifier
            .size(width = 180.dp, height = 120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = fullUrl,
                contentDescription = item.title,
                placeholder = ColorPainter(Color.LightGray),
                fallback = ColorPainter(Color.Gray),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(8.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}