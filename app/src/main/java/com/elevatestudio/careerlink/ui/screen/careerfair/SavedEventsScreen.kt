package com.elevatestudio.careerlink.ui.screen.careerfair

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedEventsScreen(
    navController: NavController,
    viewModel: CareerFairViewModel
) {

    var showDialog by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }

    Scaffold(
        containerColor = Color(0xFFF6F7FB),
        topBar = {
            TopAppBar(
                title = { Text("Event Saya", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen
                )
            )
        }
    ) { padding ->

        if (viewModel.savedEvents.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada event yang diikuti")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(viewModel.savedEvents) { event ->
                    SavedEventCard(
                        event = event,
                        onCancel = {
                            selectedEvent = event
                            showDialog = true
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        if (showDialog && selectedEvent != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.removeEvent(selectedEvent!!.id)
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
                title = { Text("Batalkan Mengikuti Event?") },
                text = { Text("Event ini akan dihapus dari daftar Event Saya.") }
            )
        }
    }
}

@Composable
fun SavedEventCard(
    event: Event,
    onCancel: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(Modifier.weight(1f)) {
                Text(
                    event.title,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${event.date} • ${event.location}",
                    color = Color.Gray
                )
            }

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
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
