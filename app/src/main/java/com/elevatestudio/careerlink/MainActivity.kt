package com.elevatestudio.careerlink

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.elevatestudio.careerlink.data.remote.RetrofitClient
import com.elevatestudio.careerlink.navigation.AppNavigation
import com.elevatestudio.careerlink.ui.theme.CareerLinkTheme
import com.elevatestudio.careerlink.utils.UserPreferences
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Panggil Fungsi Setup FCM di sini (Saat aplikasi baru dibuka)
        setupFcmToken()
        val notificationJobId = intent.getStringExtra("jobId")

        setContent {
            CareerLinkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(startJobId = notificationJobId)
                }
            }
        }
    }

    // Fungsi khusus untuk mengambil Token dan kirim ke Backend
    private fun setupFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Gagal mengambil token FCM", task.exception)
                return@addOnCompleteListener
            }

            // 1. Ambil Token Baru
            val token = task.result
            Log.d("FCM", "Token HP ini: $token")

            // 2. Kirim ke Backend (Hanya jika user sedang Login)
            sendTokenToBackend(token)
        }
    }

    private fun sendTokenToBackend(fcmToken: String) {
        // Gunakan lifecycleScope agar berjalan di background thread
        lifecycleScope.launch {
            try {
                val userPreferences = UserPreferences(applicationContext)
                // Cek apakah ada token login (User sudah login?)
                val authToken = userPreferences.authToken.first()

                if (!authToken.isNullOrEmpty()) {
                    // Panggil API update token
                    RetrofitClient.instance.updateFcmToken(
                        token = "Bearer $authToken",
                        data = mapOf("fcm_token" to fcmToken)
                    )
                    Log.d("FCM", "Token berhasil dikirim ke database!")
                } else {
                    Log.d("FCM", "User belum login, token disimpan di HP saja dulu.")
                }
            } catch (e: Exception) {
                Log.e("FCM", "Gagal kirim token ke backend: ${e.message}")
            }
        }
    }
}