package com.elevatestudio.careerlink.ui.screen.careerfair

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen
import com.elevatestudio.careerlink.data.model.CareerFairModels.Event
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CareerFairScreen(navController: NavController) {

    val eventList = listOf(
        Event(1, "Career Fair UNAND 2025", "27 Okt 2025", "Auditorium A", "Pameran kerja dan magang dari berbagai perusahaan nasional."),
        Event(2, "Networking Day 2025", "29 Okt 2025", "Hall B", "Temui HR, mentor, dan profesional startup ternama."),
        Event(3, "Tech Hiring Week", "2 Nov 2025", "Convention Center", "Pelatihan coding dan rekrutmen langsung oleh perusahaan IT.")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Career Fair & Networking", color = Color.White) },
                actions = {
                    IconButton(onClick = { navController.navigate("notification") }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifikasi", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        },
        floatingActionButton = {
            // FAB animasi berdenyut
            var expanded by remember { mutableStateOf(true) }
            val scale by animateFloatAsState(
                targetValue = if (expanded) 1.05f else 1f,
                animationSpec = tween(800),
                label = "fabScale"
            )
            LaunchedEffect(Unit) {
                while (true) {
                    expanded = !expanded
                    delay(800)
                }
            }

            ExtendedFloatingActionButton(
                onClick = { navController.navigate("eventMap") },
                containerColor = PrimaryGreen,
                elevation = FloatingActionButtonDefaults.elevation(10.dp),
                modifier = Modifier.scale(scale),
                content = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Place, contentDescription = "Peta Event", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Lihat Peta", color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(padding)
        ) {
            itemsIndexed(eventList) { index, event ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(400 * (index + 1))) + slideInVertically(initialOffsetY = { it / 2 }),
                ) {
                    EventCardItem(event = event, onClick = {
                        val encodedTitle = Uri.encode(event.title)
                        navController.navigate("eventDetail/$encodedTitle")  // ✅ sesuai dengan NavGraph
                    })
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun EventCardItem(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(listOf(SecondaryGreen.copy(alpha = 0.35f), Color.White))
                )
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = event.title,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${event.date} · ${event.location}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = event.description,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Lihat Detail", color = Color.White)
                }
            }
        }
    }
}
