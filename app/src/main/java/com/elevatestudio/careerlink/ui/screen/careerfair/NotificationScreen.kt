package com.elevatestudio.careerlink.ui.screen.careerfair

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen

data class NotificationItem(
    val id: Int,
    val title: String,
    val message: String,
    val createdattime: String,
    var isread: Boolean = false
)

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {

    var notifications by remember {
        mutableStateOf(
            mutableListOf(
                NotificationItem(1, "Event Baru!", "Career Fair UNAND telah dibuka!", "2 jam lalu", false),
                NotificationItem(2, "Pengingat Event", "Jangan lupa hadir di Networking Day!", "1 hari lalu", true)
            )
        )
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = { Text("Notifikasi", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(notifications) { index, notif ->

                NotificationItemView(
                    notif = notif,
                    onClick = {
                        notifications[index] = notifications[index].copy(isread = true)
                        navController.navigate("eventDetail/${notif.title}")
                    }
                )

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun NotificationItemView(notif: NotificationItem, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (!notif.isread) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(PrimaryGreen, shape = MaterialTheme.shapes.small)
                )
                Spacer(Modifier.width(10.dp))
            }

            Column(Modifier.weight(1f)) {
                Text(notif.title, color = PrimaryGreen)
                Text(notif.message, color = Color.DarkGray)
                Text(notif.createdattime, color = Color.Gray)
            }
        }
    }
}
