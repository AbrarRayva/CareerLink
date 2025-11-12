package com.elevatestudio.careerlink.ui.screen.careerfair

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen
import com.elevatestudio.careerlink.data.model.CareerFairModels.Booth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoothDetailScreen(navController: NavController, boothId: Int) {
    // Sementara data dummy
    val booth = when (boothId) {
        1 -> Booth(1, "Booth A", "PT TechnoNusa", "Perusahaan teknologi AI & Cloud.")
        2 -> Booth(2, "Booth B", "CV Kreatif Design", "Startup desain grafis dan branding.")
        else -> Booth(3, "Booth C", "PT Finansia", "Fintech dan rekrutmen onsite.")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Booth", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
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
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Placeholder foto booth
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(SecondaryGreen, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Foto Booth Placeholder", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(booth.name, fontWeight = FontWeight.Bold, color = PrimaryGreen)
            Text(booth.company, color = Color.Gray)
            Text(booth.desc, modifier = Modifier.padding(top = 8.dp), color = Color.DarkGray)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kembali", color = Color.White)
            }
        }
    }
}
