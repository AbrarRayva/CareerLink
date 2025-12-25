package com.elevatestudio.careerlink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.navigation.AppNavigation
import com.elevatestudio.careerlink.ui.theme.CareerLinkTheme // Ganti nama tema defaultmu

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize ApiClient with context for AuthInterceptor
        ApiClient.init(this)
        
        setContent {
            CareerLinkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}