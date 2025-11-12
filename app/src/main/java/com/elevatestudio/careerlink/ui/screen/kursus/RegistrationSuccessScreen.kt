// Lokasi: ui/screen/kursus/RegistrationSuccessScreen.kt
package com.elevatestudio.careerlink.ui.screen.kursus

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen

@Composable
fun RegistrationSuccessScreen(
    onKembaliClick: () -> Unit
) {
    // --- ANIMASI UNTUK IKON CHECK ---
    var hasAnimated by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        targetValue = if (hasAnimated) 1f else 0f,
        animationSpec = tween(durationMillis = 500, delayMillis = 100), label = "scale"
    )
    LaunchedEffect(Unit) {
        hasAnimated = true
    }
    // --- SELESAI ANIMASI ---

    Scaffold(
        containerColor = AppBackground,
        bottomBar = {
            Button(
                onClick = onKembaliClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SecondaryGreen.copy(alpha = 0.6f),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Kembali")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ikon Check (dengan modifier scale)
            Surface(
                color = SecondaryGreen.copy(alpha = 0.6f),
                shape = CircleShape,
                modifier = Modifier
                    .size(150.dp)
                    .scale(scale.value) // Terapkan animasi scale
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(24.dp)
                        .size(100.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            // Teks
            Text(
                text = "Berhasil Mendaftar",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Kami sudah mengirimkan detail kelas ke email kamu.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}