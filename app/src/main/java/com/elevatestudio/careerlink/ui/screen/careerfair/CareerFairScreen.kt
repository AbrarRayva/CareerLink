package com.elevatestudio.careerlink.ui.screen.careerfair

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elevatestudio.careerlink.ui.components.AppBottomNavBar
import com.elevatestudio.careerlink.data.model.CareerFairModels.Event
import com.elevatestudio.careerlink.navigation.Routes
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CareerFairScreen(navController: NavController) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val eventList = listOf(
        Event(1, "Career Fair UNAND 2025", "27 Okt 2025", "Auditorium A",
            "Pameran kerja dan magang dari berbagai perusahaan nasional."),
        Event(2, "Networking Day 2025", "29 Okt 2025", "Hall B",
            "Temui HR, mentor, dan profesional startup ternama."),
        Event(3, "Tech Hiring Week", "2 Nov 2025", "Convention Center",
            "Pelatihan coding dan rekrutmen langsung oleh perusahaan IT.")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(260.dp)
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    "Menu",
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )

                DrawerItem("Home") {
                    scope.launch { drawerState.close() }
                }

                DrawerItem("Saved Event") {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.SAVED_EVENTS)
                }

                DrawerItem("Networking") {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.NETWORKING)
                }
            }
        }
    ) {

        Scaffold(
            containerColor = Color.White,
            topBar = {
                TopAppBar(
                    title = { Text("Career Fair & Networking", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(Routes.NOTIFICATION)
                        }) {
                            Icon(
                                Icons.Filled.Notifications,
                                contentDescription = "Notifikasi",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PrimaryGreen
                    )
                )
            },


            bottomBar = {
                AppBottomNavBar(
                    currentRoute = "event",
                    onItemSelected = { route ->
                        when (route) {
                            "event" -> {
                                navController.navigate(Routes.CAREER_FAIR)
                            }
                            "kursus" -> {
                                navController.navigate(Routes.KURSUS_DASHBOARD)
                            }
                            "home" -> {
                                navController.navigate(Routes.ONBOARDING)
                            }
                            "mentor" -> {
                                navController.navigate(Routes.JADWAL_MENTORING)
                            }
                            "lowongan" -> {
                                navController.navigate(Routes.DAFTAR_LOWONGAN)
                            }
                        }
                    }
                )
            },

            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate(Routes.EVENT_MAP)
                    },
                    containerColor = PrimaryGreen
                ) {
                    Icon(Icons.Filled.Place, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Lihat Peta Event", color = Color.White)
                }
            }
        ) { padding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                itemsIndexed(eventList) { index, event ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(tween(300)) +
                                slideInVertically(initialOffsetY = { it / 2 })
                    ) {
                        EventCardItem(
                            event = event,
                            onClick = {
                                val encoded = Uri.encode(event.title)
                                navController.navigate("eventDetail/$encoded")
                            }
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

/* ================= DRAWER ITEM ================= */

@Composable
private fun DrawerItem(title: String, onClick: () -> Unit) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        fontWeight = FontWeight.Medium
    )
}

/* ================= EVENT CARD ================= */

@Composable
fun EventCardItem(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(
                            SecondaryGreen.copy(alpha = 0.7f),
                            Color.White
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Text(
                    event.title,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "${event.date} · ${event.location}",
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    event.description,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(10.dp))

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
