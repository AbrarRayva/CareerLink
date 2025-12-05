package com.elevatestudio.careerlink.ui.screen.careerfair

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elevatestudio.careerlink.data.model.CareerFairModels.Event
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import java.time.LocalDate

@SuppressLint("MutableCollectionMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedEventsScreen(navController: NavController) {

    var savedEvents by remember {
        mutableStateOf(
            mutableListOf(
                Event(1, "Career Fair UNAND 2025", "2025-10-27", "Auditorium A", "Deskripsi event"),
                Event(2, "Tech Hiring Week", "2025-11-02", "Convention Hall", "Deskripsi event")
            )
        )
    }

    var showDialog by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event yang Diikuti", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(savedEvents) { event ->
                    SavedEventItem(
                        event = event,
                        onClick = {
                            val encoded = java.net.URLEncoder.encode(event.title, "UTF-8")
                            navController.navigate("eventDetail/$encoded")
                        },
                        onCancel = {
                            selectedEvent = event
                            showDialog = true
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        // konfirmasi batal mengikuti
        if (showDialog && selectedEvent != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        savedEvents.remove(selectedEvent)
                        selectedEvent = null
                        showDialog = false
                    }) {
                        Text("Ya, Batalkan", color = PrimaryGreen)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Tidak")
                    }
                },
                title = { Text("Batalkan Mengikuti?") },
                text = {
                    Text("Apakah Anda yakin ingin berhenti mengikuti event ini?")
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SavedEventItem(
    event: Event,
    onClick: () -> Unit,
    onCancel: () -> Unit
) {
    val today = LocalDate.now()
    val eventDate = LocalDate.parse(event.date)
    val canCancel = eventDate.isAfter(today)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(Modifier.weight(1f)) {
                Text(event.title, fontWeight = FontWeight.Bold, color = PrimaryGreen)
                Text("${event.date} • ${event.location}", color = Color.Gray)
            }

            if (canCancel) {
                Box {
                    var expanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = null)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Batalkan Mengikuti") },
                            onClick = {
                                expanded = false
                                onCancel()
                            }
                        )
                    }
                }
            }
        }
    }
}
