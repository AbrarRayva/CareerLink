package com.elevatestudio.careerlink.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.elevatestudio.careerlink.ui.components.AppLogo
import com.elevatestudio.careerlink.ui.theme.AppBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    // Ini parameter fungsi biar bisa ngasih tau "Splash udah selesai, pindah layar!"
    onSplashFinished: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground), // Pakai warna background kita
        contentAlignment = Alignment.Center // Biar logonya di tengah
    ) {
        AppLogo(modifier = Modifier.fillMaxSize(0.3f)) // Logo dibikin 30% ukuran layar

        // Efek ini jalan sekali pas layar kebuka
        LaunchedEffect(key1 = true) {
            delay(2000L) // Tunggu 2 detik (pura-pura loading)
            onSplashFinished() // Panggil fungsi buat pindah layar
        }
    }
}