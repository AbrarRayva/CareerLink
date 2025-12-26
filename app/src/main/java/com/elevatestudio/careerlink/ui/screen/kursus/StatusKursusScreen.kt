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
import com.elevatestudio.careerlink.data.model.KursusItem
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusKursusScreen(
    statusType: String, // "Active" atau "Completed"
    onBackClick: () -> Unit,
    onDetailClick: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // State List
    var listKursus by remember { mutableStateOf<List<KursusItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            val token = UserPreferences(context).authToken.first() ?: ""
            try {
                // PANGGIL API 'Enrolled List' YANG BARU KITA BUAT
                val response = ApiClient.instance.getMyEnrolledCourses("Bearer $token", statusType)

                if (response.isSuccessful && response.body()?.success == true) {
                    // Sekarang listKursus HANYA berisi data yang filternya pas (Aktif saja atau Selesai saja)
                    listKursus = response.body()!!.data
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = { Text("Kursus $statusType") }, // Judul sesuai tombol (Aktif/Selesai)
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Kembali") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else if (listKursus.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Belum ada kursus di status ini", color = Color.Gray)
            }
        } else {
            LazyColumn(Modifier.padding(padding).padding(16.dp)) {
                items(listKursus) { item ->
                    KursusListCard(item = item, onClick = { onDetailClick(item.id.toString()) })
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}