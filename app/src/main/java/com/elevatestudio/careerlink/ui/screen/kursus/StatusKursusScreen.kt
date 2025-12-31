package com.elevatestudio.careerlink.ui.screen.kursus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusKursusScreen(
    statusType: String,
    onBackClick: () -> Unit,
    onDetailClick: (String) -> Unit
) {
    val viewModel: KursusViewModel = viewModel()
    val myCourses by viewModel.myCourses.collectAsState()

   
    val filteredList = remember(myCourses, statusType) {
        val cleanStatus = statusType.replace("status_", "", ignoreCase = true)
        val isLookingForCompleted = cleanStatus.equals("completed", ignoreCase = true)

        myCourses.filter { course ->
           
            val statusApi = course.enrollmentStatus ?: course.status ?: ""

            if (isLookingForCompleted) {
                statusApi.equals("Selesai", ignoreCase = true) ||
                        statusApi.equals("Completed", ignoreCase = true) ||
                        statusApi.equals("Lulus", ignoreCase = true)
            } else {
                !statusApi.equals("Selesai", ignoreCase = true) &&
                        !statusApi.equals("Completed", ignoreCase = true) &&
                        !statusApi.equals("Lulus", ignoreCase = true)
            }
        }
    }

    val pageTitle = if (statusType.contains("completed")) "Kursus Selesai" else "Kursus Aktif"

    LaunchedEffect(statusType) {
       
        viewModel.getMyCourses(statusType)
    }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = { Text(pageTitle) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Kembali") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
            )
        }
    ) { padding ->
        if (filteredList.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Belum ada kursus di sini", color = Color.Gray)
                }
            }
        } else {
            LazyColumn(Modifier.padding(padding).padding(16.dp)) {
                items(filteredList) { item ->
                   
                    KursusListCard(item = item, onClick = { onDetailClick(item.id.toString()) })
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}