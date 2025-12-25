package com.elevatestudio.careerlink.ui.screen.kursus

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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import coil.compose.AsyncImage
import com.elevatestudio.careerlink.data.model.BadgeItem
import com.elevatestudio.careerlink.data.model.EnrolledCourse
import com.elevatestudio.careerlink.ui.theme.AppBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCoursesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetailKursus: (String) -> Unit,
    onNavigateToBadgeScan: () -> Unit,
    viewModel: KursusViewModel = viewModel()
) {
    // Load data when screen appears
    LaunchedEffect(Unit) {
        viewModel.getEnrolledCourses()
        viewModel.getBadges()
        viewModel.getCourseStats()
    }
    
    // Observe data from ViewModel
    val enrolledCourses by viewModel.enrolledCourses.collectAsState()
    val badges by viewModel.badges.collectAsState()
    val courseStats by viewModel.courseStats.collectAsState()
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
                        courseStats?.let { stats ->
                            Text("Total Course Diambil: ${stats.totalCourses}")
                            Text("Course Selesai: ${stats.completedCourses}")
                            Text("Course Aktif: ${stats.activeCourses}")
                        } ?: run {
                            Text("Total Course Diambil: ${enrolledCourses.size}")
                            Text("Course Selesai: ${enrolledCourses.count { it.status == "Completed" }}")
                            Text("Course Aktif: ${enrolledCourses.count { it.status == "Active" }}")
                        }
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

                if (badges.isEmpty()) {
                    Text(
                        text = "Anda belum memiliki badge. Selesaikan course untuk mendapatkannya!",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(badges) { badge ->
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

            items(enrolledCourses) { kursus ->
                KursusItemCard(
                    kursus = kursus,
                    onClick = { onNavigateToDetailKursus(kursus.id.toString()) },
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
        AsyncImage(
            model = badge.imageUrl ?: "",
            contentDescription = "Badge untuk ${badge.title}",
            placeholder = androidx.compose.ui.graphics.painter.ColorPainter(Color.LightGray),
            fallback = androidx.compose.ui.graphics.painter.ColorPainter(Color.Gray),
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = badge.title,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun KursusItemCard(kursus: EnrolledCourse, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = kursus.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Oleh: ${kursus.providerName}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Status: ${kursus.status}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
