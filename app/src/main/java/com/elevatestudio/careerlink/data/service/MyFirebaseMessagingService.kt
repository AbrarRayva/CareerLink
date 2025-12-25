package com.elevatestudio.careerlink.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.elevatestudio.careerlink.MainActivity // Pastikan import MainActivity kamu benar
import com.elevatestudio.careerlink.R
import com.elevatestudio.careerlink.data.remote.RetrofitClient
import com.elevatestudio.careerlink.utils.UserPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // 1. Dijalankan saat HP pertama kali dapat Token atau Token berubah
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Kita harus kirim token ini ke Backend agar Backend tau kemana harus kirim notif
        sendTokenToBackend(token)
    }

    // 2. Dijalankan saat ada pesan masuk (Saat aplikasi dibuka)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Tampilkan notifikasi
        remoteMessage.notification?.let {
            showNotification(it.title ?: "Info", it.body ?: "Ada pesan baru")
        }
    }

    private fun showNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "CareerLink_Channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ganti icon notif kamu
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Untuk Android Oreo ke atas wajib pake Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel CareerLink",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun sendTokenToBackend(token: String) {
        // Panggil API update FCM Token (Nanti kita buat di Backend)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val context = applicationContext
                val prefs = UserPreferences(context)
                val userToken = prefs.authToken.first()

                if (!userToken.isNullOrEmpty()) {
                    // Panggil API (Kita buat nanti di ApiService)
                    RetrofitClient.instance.updateFcmToken("Bearer $userToken", mapOf("fcm_token" to token))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}