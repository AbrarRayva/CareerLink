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
   
    onSplashFinished: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground),
        contentAlignment = Alignment.Center
    ) {
        AppLogo(modifier = Modifier.fillMaxSize(0.3f))

       
        LaunchedEffect(key1 = true) {
            delay(2000L)
            onSplashFinished()
        }
    }
}