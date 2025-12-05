package com.elevatestudio.careerlink.ui.screen.careerfair

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventMapScreen(
    navController: NavController,
    mode: String?,      // "event" atau "booth"
    eventId: Int?       // hanya dipakai kalau mode = "booth"
) {

    val safeMode = mode ?: "event"   // default = event

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (safeMode == "booth") "Peta Booth" else "Peta Event", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when (safeMode) {

                "event" -> EventGoogleMapView()

                "booth" -> BoothCustomMapView(
                    navController = navController,
                    eventId = eventId ?: -1
                )

                else -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { Text("Mode tidak dikenali") }
            }
        }
    }
}

@Composable
fun BoothCustomMapView(
    navController: NavController, // Add this parameter
    eventId: Int                  // Add this parameter
) {
    // Placeholder UI to prevent crashing
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Booth Map View - Under Construction (Event ID: $eventId)")
    }
}

@Composable
fun EventGoogleMapView() {
    // TODO: Implement this later
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Event Google Map View")
    }
}
