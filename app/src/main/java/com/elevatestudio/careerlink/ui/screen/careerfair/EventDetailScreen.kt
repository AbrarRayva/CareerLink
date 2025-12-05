package com.elevatestudio.careerlink.ui.screen.careerfair

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun EventDetailScreen(
    navController: NavController,
    eventTitle: String?,
) {
    val title = eventTitle ?: "Career Fair Event"
    var isFollowed by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Event", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.White, SecondaryGreen.copy(0.1f))))
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                    .background(PrimaryGreen),
                contentAlignment = Alignment.Center
            ) {
                Text("Banner Event Placeholder", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(20.dp))

            Column(Modifier.padding(20.dp)) {

                Text(
                    title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    "27 Okt 2025 • Auditorium A",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    "Deskripsi lengkap event akan ditampilkan di sini.",
                    textAlign = TextAlign.Justify
                )

                Spacer(Modifier.height(30.dp))

                Button(
                    onClick = { navController.navigate("eventMap?mode=booth&eventId=1") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("📍 Lihat Lokasi Booth", color = Color.White)
                }

                Spacer(Modifier.height(14.dp))

                OutlinedButton(
                    onClick = { isFollowed = !isFollowed },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryGreen)
                ) {
                    Text(if (isFollowed) "✔ Mengikuti Event" else "💚 Ikuti Event Ini")
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}
