package com.elevatestudio.careerlink.ui.screen.kursus

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.elevatestudio.careerlink.data.model.BadgeItem
import com.elevatestudio.careerlink.data.model.Course
import com.elevatestudio.careerlink.data.model.UserStats
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.ui.components.AppBottomNavBar
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.utils.UserPreferences
import kotlinx.coroutines.flow.first
import android.util.Log
import kotlinx.coroutines.launch


@Composable
fun DashboardKursusScreen(
    onNavigate: (String) -> Unit,
    onNavigateToDaftarKursus: () -> Unit,
    onNavigateToDetailKursus: (String) -> Unit,
    onNavigateToBadgeScan: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: KursusViewModel = viewModel()

    val rekomendasi by viewModel.recommendedCourses.collectAsState()
    val badges by viewModel.badgeState.collectAsState()
    val stats by viewModel.stats.collectAsState()

    var username by remember { mutableStateOf("User") }

    LaunchedEffect(Unit) {
        val name = UserPreferences(context).userName.first()
        if (!name.isNullOrEmpty()) username = name
        viewModel.refreshDashboard()
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryGreen)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "Skill & Sertifikasi",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Halo, $username 👋",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tingkatkan skillmu hari ini!",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        },
        bottomBar = { AppBottomNavBar("kursus", onNavigate) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp)
        ) {

           
            item {
                Text("Koleksi Badge Saya", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(badges) { badge -> BadgeCircleItem(badge) }
                    item { AddBadgeButton(onClick = onNavigateToBadgeScan) }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

           
            item {
                Text("Statistik Belajar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                StatsCard(stats, onNavigate)
                Spacer(modifier = Modifier.height(24.dp))
            }

           
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Rekomendasi Untukmu", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    TextButton(onClick = onNavigateToDaftarKursus) {
                        Text("Lihat Semua", color = PrimaryGreen, fontSize = 12.sp)
                    }
                }

                if (rekomendasi.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                        Text("Belum ada rekomendasi", color = Color.Gray, fontSize = 12.sp)
                    }
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(rekomendasi) { kursus ->
                            KursusRekomendasiCard(kursus) { onNavigateToDetailKursus(kursus.id.toString()) }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun BadgeCircleItem(item: BadgeItem) {
   
    val baseUrl = ApiClient.BASE_URL

   
   
   
    var cleanPath = item.imageUrl?.replace("\\", "/") ?: ""
    cleanPath = cleanPath.removePrefix("public/")
    cleanPath = cleanPath.removePrefix("/")      

    val fullUrl = if (cleanPath.startsWith("http")) {
        cleanPath
    } else {
        "${baseUrl.removeSuffix("/")}/$cleanPath"
    }

   
    android.util.Log.d("BadgeURL", "Original: ${item.imageUrl} -> Final: $fullUrl")

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(end = 8.dp)) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, PrimaryGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(fullUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Badge",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                   
                    CircularProgressIndicator(modifier = Modifier.padding(15.dp), color = PrimaryGreen, strokeWidth = 2.dp)
                },
                error = {
                   
                    Icon(Icons.Default.EmojiEvents, null, tint = Color(0xFFFFC107), modifier = Modifier.size(30.dp))
                }
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(item.name, fontSize = 10.sp, maxLines = 1)
    }
}

@Composable
fun AddBadgeButton(onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9))
                .clickable { onClick() }
                .border(1.dp, PrimaryGreen, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, null, tint = PrimaryGreen, modifier = Modifier.size(30.dp))
        }
        Spacer(Modifier.height(4.dp))
        Text("Tambah", fontSize = 10.sp)
    }
}

@Composable
fun StatsCard(stats: UserStats, onNavigate: (String) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatItem(stats.activeCourses.toString(), "Aktif", Icons.Default.School, Color(0xFF2196F3)) { onNavigate("status_active") }
            StatItem(stats.completedCourses.toString(), "Selesai", Icons.Default.Verified, PrimaryGreen) { onNavigate("status_completed") }
            StatItem(stats.totalBadges.toString(), "Badge", Icons.Default.EmojiEvents, Color(0xFFFFC107)) { onNavigate("badge_scan") }
        }
    }
}

@Composable
fun StatItem(value: String, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun KursusRekomendasiCard(item: Course, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(200.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box(Modifier.fillMaxWidth().height(100.dp).background(PrimaryGreen.copy(0.1f)), Alignment.Center) {
                Icon(Icons.Default.School, null, tint = PrimaryGreen, modifier = Modifier.size(40.dp))
            }
            Column(Modifier.padding(12.dp)) {
                Text(item.title, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(item.role ?: "Elevate Academy", fontSize = 10.sp, color = PrimaryGreen)
            }
        }
    }
}