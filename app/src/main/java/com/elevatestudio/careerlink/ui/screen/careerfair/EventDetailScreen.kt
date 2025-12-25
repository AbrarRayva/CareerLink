package com.elevatestudio.careerlink.ui.screen.careerfair

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elevatestudio.careerlink.data.model.CareerFairModels.Event
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun EventDetailScreen(
    navController: NavController,
    eventTitle: String?,
    viewModel: CareerFairViewModel
) {

    val event = Event(
        id = 1,
        title = eventTitle ?: "Career Fair Event",
        date = "2025-10-27",
        location = "Auditorium A",
        description = "Deskripsi lengkap event akan ditampilkan di sini."
    )


    val isFollowed = viewModel.isEventSaved(event.id)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Event", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White, SecondaryGreen.copy(alpha = 1.5f))
                    )
                )
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
                Text(
                    "Banner Event Placeholder",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(20.dp))

            Column(Modifier.padding(20.dp)) {

                Text(
                    event.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    "${event.date} • ${event.location}",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    event.description,
                    color = Color.Black,
                    textAlign = TextAlign.Justify
                )

                Spacer(Modifier.height(30.dp))

                Button(
                    onClick = {
                        navController.navigate("boothMap")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("📍 Lihat Lokasi Booth", color = Color.White)
                }

                Spacer(Modifier.height(14.dp))

                OutlinedButton(
                    onClick = {
                        if (isFollowed) {
                            viewModel.removeEvent(event.id)
                        } else {
                            viewModel.saveEvent(event)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PrimaryGreen
                    )
                ) {
                    Text(
                        if (isFollowed) "✔ Mengikuti Event"
                        else "💚 Ikuti Event Ini"
                    )
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}
