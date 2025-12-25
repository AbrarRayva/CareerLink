// Lokasi: ui/screen/kursus/DashboardKursusScreen.kt
package com.elevatestudio.careerlink.ui.screen.kursus

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Search
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.elevatestudio.careerlink.data.local.TokenManager
import com.elevatestudio.careerlink.data.model.BadgeItem
import com.elevatestudio.careerlink.ui.components.AppBottomNavBar
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardKursusScreen(
    onNavigate: (String) -> Unit,
    onNavigateToDaftarKursus: () -> Unit,
    onNavigateToDetailKursus: (String) -> Unit, // Keep for future use
    onNavigateToBadgeScan: () -> Unit,
    onNavigateToMyCourses: () -> Unit,
    viewModel: KursusViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    
    // Get user's first name from full_name
    val userFullName = remember { tokenManager.getUserFullName() }
    val firstName = remember(userFullName) {
        userFullName?.split(" ")?.firstOrNull() ?: "User"
    }
    
    var searchQuery by remember { mutableStateOf("") }
    
    // Load data when screen first appears
    LaunchedEffect(Unit) {
        viewModel.getRecommendedCourses()
        viewModel.getBadges()
    }
    
    // Observe data from ViewModel
    val recommendedCourses by viewModel.recommendedCourses.collectAsState()
    val badges by viewModel.badges.collectAsState()

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
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            // 1. Halo, Username
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Halo, $firstName",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Selamat datang kembali!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }

            // 2. Card "Course & Badge Saya"
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToMyCourses() },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0FE)), // Warna biru muda
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.LibraryBooks,
                            contentDescription = "My Courses Icon",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Course & Badge Saya",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Lihat semua progress belajarmu di sini.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Navigate to My Courses"
                        )
                    }
                }
            }

            // 3. Preview Badge
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Badge Terbaru",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (badges.isEmpty()) {
                    Text(
                        text = "Anda belum mendapatkan badge. Selesaikan course untuk mendapatkannya!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(
                            items = badges,
                            key = { badge -> badge.id }
                        ) { badge ->
                            AsyncImage(
                                model = badge.imageUrl ?: "",
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

            // 4. Cari Course
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Cari Course",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Ketik nama course...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Atau lihat semua daftar course yang tersedia.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { onNavigateToDaftarKursus() }
                        .padding(start = 4.dp)
                )

            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}
