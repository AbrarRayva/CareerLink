package com.elevatestudio.careerlink.ui.mentoring


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tbptb.ui.theme.mentoring.model.MentoringSession
import com.example.tbptb.ui.theme.mentoring.viewmodel.MentoringViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalMentoringScreen(navController: NavController) {
    val viewModel: MentoringViewModel = viewModel()
    val sessions by viewModel.mentoringSessions.collectAsState()
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            // Menggunakan CenterAlignedTopAppBar (M3)
            CenterAlignedTopAppBar(
                title = { Text("Jadwal Mentoring") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        // PERBAIKAN: Menggunakan ikon AutoMirrored
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            // Search Bar
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Search") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Daftar Sesi (LazyColumn)
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(sessions) { session ->
                    MentoringItem(session) {
                        navController.navigate("detail_mentoring/${session.id}")
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun MentoringItem(session: MentoringSession, onClick: () -> Unit) {
    val cardColor = if (session.nama == "Jack") Color(0xFFF0E5E7) else Color(0xFFE5F0E7)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.5f))
            ) {
                // Placeholder Image/Icon
            }
            Spacer(Modifier.width(16.dp))
            Column {
                InfoRow("Nama", session.nama, boldValue = true)
                InfoRow("Pekerjaan", session.pekerjaan)
                InfoRow("Tanggal", session.tanggal)
                InfoRow("Jam", session.jam)
                InfoRow("Tempat", session.tempat)
                Text("Details", color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp))
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, boldValue: Boolean = false) {
    Row {
        Text("$label", modifier = Modifier.width(70.dp), style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
        Text(": ", style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
        Text(value, style = MaterialTheme.typography.bodySmall,
            fontWeight = if (boldValue) FontWeight.Bold else FontWeight.Normal)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMentoringScreen(navController: NavController, sessionId: String?) {
    val viewModel: MentoringViewModel = viewModel()
    val session = viewModel.getSessionById(sessionId)

    if (session == null) {
        Scaffold(
            topBar = { CenterAlignedTopAppBar(title = { Text("Jadwal Mentoring") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali") } }) }
        ) { padding ->
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Sesi tidak ditemukan.", style = MaterialTheme.typography.headlineSmall)
            }
        }
        return
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Jadwal Mentoring") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = { navController.navigate("booking_mentoring/${session.id}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
            ) {
                Text("Book Now", style = MaterialTheme.typography.titleMedium)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection(session)
            DetailsSection(session)
            MapsSection(session.mapsCoordinates)
        }
    }
}

@Composable
fun HeaderSection(session: MentoringSession) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF8FBC94))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color(0xFFD2B48C))
        ) {
        }
        Spacer(Modifier.height(24.dp))
        DetailInfoRow("Nama", session.nama)
        DetailInfoRow("Pekerjaan", session.pekerjaan)
        DetailInfoRow("Tanggal", session.tanggal)
        DetailInfoRow("Jam", session.jam)
    }
}

@Composable
fun DetailInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "$label",
            modifier = Modifier.width(100.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Text(": ", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = Color.White)
        Text(
            value,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }
}

@Composable
fun DetailsSection(session: MentoringSession) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Details", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(session.detail, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        Text("*Catatan: Mohon siapkan dan kirimkan link portofolio Anda minimal 4 jam sebelum sesi dimulai.",
            style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Spacer(Modifier.height(16.dp))
        Text(session.catatanPlatform, style = MaterialTheme.typography.bodyMedium)
        Text("Durasi: ${session.durasi}", style = MaterialTheme.typography.bodyMedium)
        Text("Time Zone: ${session.timeZone}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        Divider(Modifier.padding(vertical = 8.dp))
        Text("\"Halo! Saya sudah tidak sabar untuk me-review hasil kerja keras Anda. Jangan sungkan untuk mencatat semua pertanyaan Anda sebelumnya. Sampai jumpa di sesi!\"",
            style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic)
        Text("- ${session.nama} -", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp).align(Alignment.End))
        Divider(Modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun MapsSection(coordinates: String) {
    Column(modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)) {
        Text("Maps", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.LocationOn, contentDescription = "Lokasi", tint = Color.Red, modifier = Modifier.size(48.dp))
        }
    }
}