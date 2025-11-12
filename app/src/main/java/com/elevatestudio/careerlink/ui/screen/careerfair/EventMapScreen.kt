package com.elevatestudio.careerlink.ui.screen.careerfair

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen
import com.elevatestudio.careerlink.data.model.CareerFairModels.Booth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventMapScreen(navController: NavController) {
    var selectedBooth by remember { mutableStateOf<Booth?>(null) }

    val boothList = listOf(
        Booth(1, "Booth A", "PT TechnoNusa", "Perusahaan teknologi AI & Cloud."),
        Booth(2, "Booth B", "CV Kreatif Design", "Startup desain grafis dan branding."),
        Booth(3, "Booth C", "PT Finansia", "Fintech dan rekrutmen onsite.")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lokasi Event", color = Color.White) },
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
        },
        containerColor = Color.White
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // area peta
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE8EAE9))
            ) {
                // Titik booth (pin)
                boothList.forEachIndexed { index, booth ->
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .offset(
                                x = (40 * index + 50).dp,
                                y = (80 * index + 20).dp
                            )
                            .clip(CircleShape)
                            .background(PrimaryGreen)
                            .clickable { selectedBooth = booth },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "📍",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Jika booth dipilih, tampilkan panel bawah
            selectedBooth?.let { booth ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF8F8F8))
                        .padding(16.dp)
                ) {
                    Text(
                        booth.name,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                    Text(booth.company, color = Color.Gray)
                    Text(booth.desc, color = Color.DarkGray, modifier = Modifier.padding(top = 8.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            navController.navigate("boothDetail/${booth.id}")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Lihat Booth", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { navController.navigate("checkIn") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SecondaryGreen)
                    ) {
                        Text("Scan Check-In", color = SecondaryGreen)
                    }
                }
            }
        }
    }
}
