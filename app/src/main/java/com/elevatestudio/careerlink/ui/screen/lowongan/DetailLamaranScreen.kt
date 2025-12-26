package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.elevatestudio.careerlink.data.model.ApplicationDetail
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailLamaranScreen(
    applicationId: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var detail by remember { mutableStateOf<ApplicationDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(applicationId) {
        scope.launch {
            val token = UserPreferences(context).authToken.first() ?: ""
            if (token.isNotEmpty()) {
                try {
                    val response = ApiClient.instance.getDetailLamaran("Bearer $token", applicationId)
                    if (response.isSuccessful) {
                        detail = response.body()
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Status Lamaran") },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PrimaryGreen)
            } else if (detail != null) {
                val data = detail!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(data.jobTitle, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
                    Text(data.companyName, style = MaterialTheme.typography.titleMedium, color = Color.Gray)

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Status Lamaran", fontWeight = FontWeight.Bold)
                            Text(data.status, color = PrimaryGreen, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Dikirim pada: ${data.appliedAt}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                   
                    if (!data.fullName.isNullOrEmpty()) {
                        Text("Data Pelamar", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Nama: ${data.fullName}")
                        Text("Email/HP: ${data.phoneNumber}")
                    }
                }
            } else {
                Text("Gagal memuat detail", modifier = Modifier.align(Alignment.Center), color = Color.Red)
            }
        }
    }
}