package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import com.elevatestudio.careerlink.data.model.RiwayatItem
import com.elevatestudio.careerlink.data.remote.RetrofitClient
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
    var listRiwayat by remember { mutableStateOf<List<RiwayatItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Load Data
    LaunchedEffect(Unit) {
        scope.launch {
            val userPreferences = UserPreferences(context)
            val token = userPreferences.authToken.first()
            if (!token.isNullOrEmpty()) {
                try {
                    val response = RetrofitClient.instance.getRiwayatLamaran("Bearer $token")
                    if (response.isSuccessful) {
                        listRiwayat = response.body() ?: emptyList()
                    }
                } catch (e: Exception) {

                }
            }
            isLoading = false
        }
    }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Lamaran", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else if (listRiwayat.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Belum ada lamaran yang dikirim.")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
                items(listRiwayat) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clickable { onDetailClick(item.applicationId.toString()) },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(item.companyName, color = Color.Gray, fontSize = 14.sp)
                            Spacer(Modifier.height(8.dp))

                            Surface(
                                color = if (item.status == "Pending") Color(0xFFFFF3E0) else Color(0xFFE8F5E9),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Status: ${item.status}",
                                    modifier = Modifier.padding(8.dp),
                                    color = if (item.status == "Pending") Color(0xFFE65100) else PrimaryGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}