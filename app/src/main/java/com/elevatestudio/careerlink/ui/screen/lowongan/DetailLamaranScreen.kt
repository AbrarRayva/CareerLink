package com.elevatestudio.careerlink.ui.screen.lowongan

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.elevatestudio.careerlink.data.model.ApplicationDetail
import com.elevatestudio.careerlink.data.remote.ApiClient // Import ini
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
    var data by remember { mutableStateOf<ApplicationDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    fun openFile(urlPart: String) {
        val cleanUrl = urlPart.replace("\\", "/")
        val fullUrl = "${ApiClient.BASE_URL}$cleanUrl"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
        context.startActivity(intent)
    }

    LaunchedEffect(Unit) {
        scope.launch {
            val token = UserPreferences(context).authToken.first()
            if (token != null) {
                try {
                    val response = ApiClient.instance.getDetailLamaran("Bearer $token", applicationId)
                    if (response.isSuccessful) {
                        data = response.body()
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
                title = { Text("Detail Lamaran", color = Color.White) },
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
        } else if (data == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Data tidak ditemukan")
            }
        } else {
            val item = data!!
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = if (item.logoUrl?.startsWith("http") == true) item.logoUrl else "${ApiClient.BASE_URL}${item.logoUrl}",
                                contentDescription = null,
                                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(item.jobTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(item.companyName, color = Color.Gray)
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Divider()
                        Spacer(Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Status Lamaran:", fontWeight = FontWeight.Bold)

                            val statusColor = when(item.status) {
                                "Accepted" -> PrimaryGreen
                                "Rejected" -> Color.Red
                                else -> Color(0xFFE65100) // Orange
                            }
                            Text(item.status, color = statusColor, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text("Dikirim pada: ${item.appliedAt.take(10)}", fontSize = 12.sp, color = Color.Gray)
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Data Pelamar", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(16.dp)) {
                        DetailRow(Icons.Default.Person, "Nama", item.fullName)
                        DetailRow(Icons.Default.CalendarToday, "Tgl Lahir", item.dob)
                        DetailRow(Icons.Default.Transgender, "Gender", item.gender)
                        DetailRow(Icons.Default.School, "Pendidikan", "${item.education} - ${item.major}")
                        DetailRow(Icons.Default.Phone, "No HP", item.phoneNumber)

                        Spacer(Modifier.height(8.dp))
                        Text("Tentang Saya:", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text(item.aboutMe, fontSize = 13.sp, color = Color.DarkGray)
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Dokumen", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

                Button(
                    onClick = { openFile(item.cvUrl) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = PrimaryGreen),
                    border = BorderStroke(1.dp, PrimaryGreen)
                ) {
                    Icon(Icons.Default.Description, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Lihat CV")
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = { openFile(item.letterUrl) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = PrimaryGreen),
                    border = BorderStroke(1.dp, PrimaryGreen)
                ) {
                    Icon(Icons.Default.Description, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Lihat Surat Rekomendasi")
                }

                if (item.portfolioUrl != null) {
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { openFile(item.portfolioUrl) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = PrimaryGreen),
                        border = BorderStroke(1.dp, PrimaryGreen)
                    ) {
                        Icon(Icons.Default.Description, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Lihat Portofolio")
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, Modifier.size(16.dp), tint = Color.Gray)
        Spacer(Modifier.width(8.dp))
        Column {
            Text(label, fontSize = 11.sp, color = Color.Gray)
            Text(value, fontSize = 14.sp, color = Color.Black)
        }
    }
}