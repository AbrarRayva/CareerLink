package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.elevatestudio.careerlink.data.model.RiwayatItem
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatLamaranScreen(
    onBackClick: () -> Unit,
    onDetailClick: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var riwayatList by remember { mutableStateOf<List<RiwayatItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            val token = UserPreferences(context).authToken.first() ?: ""
            if (token.isNotEmpty()) {
                try {
                    val response = ApiClient.instance.getRiwayatLamaran("Bearer $token")
                    if (response.isSuccessful) {
                        riwayatList = response.body() ?: emptyList()
                    }
                } catch (e: Exception) { }
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Lamaran") },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PrimaryGreen)
            } else if (riwayatList.isEmpty()) {
                Text("Belum ada lamaran", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    items(riwayatList) { item ->
                        RiwayatItemCard(item, onClick = { onDetailClick(item.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun RiwayatItemCard(item: RiwayatItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.jobTitle, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(item.companyName, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.status, color = if (item.status == "Accepted") PrimaryGreen else Color.Gray, fontWeight = FontWeight.Bold)
        }
    }
}