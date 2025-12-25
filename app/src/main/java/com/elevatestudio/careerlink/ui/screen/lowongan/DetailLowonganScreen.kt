package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.elevatestudio.careerlink.data.model.LowonganDetail
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.ui.components.PrimaryButton
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen
import com.elevatestudio.careerlink.ui.theme.TextBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailLowonganScreen(
    lowonganId: String,
    onDaftarClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: DaftarLowonganViewModel = viewModel()
    val uiState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(lowonganId) {
        viewModel.getDetailLowongan(lowonganId)
    }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = { Text("Detail Lowongan", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        },
        bottomBar = {
            if (uiState is DetailUiState.Success) {
                val detail = (uiState as DetailUiState.Success).data
                PrimaryButton(
                    text = "DAFTAR SEKARANG",
                    onClick = { onDaftarClick(detail.id.toString()) },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (uiState) {
                is DetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DetailUiState.Error -> {
                    val msg = (uiState as DetailUiState.Error).message
                    Text(
                        text = msg,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DetailUiState.Success -> {
                    val detail = (uiState as DetailUiState.Success).data
                    DetailContent(detail)
                }
                else -> {}
            }
        }
    }
}

@Composable
fun DetailContent(detail: LowonganDetail) {
    val fullLogoUrl = if (detail.logoUrl != null && !detail.logoUrl.startsWith("http")) {
        "${ApiClient.BASE_URL}${detail.logoUrl}"
    } else {
        detail.logoUrl
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = fullLogoUrl,
                    contentDescription = null,
                    fallback = rememberVectorPainter(Icons.Default.Apartment),
                    error = rememberVectorPainter(Icons.Default.Apartment),
                    placeholder = rememberVectorPainter(Icons.Default.Apartment),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5F5F5))
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = detail.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
                Text(
                    text = detail.companyName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
            }
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoRow(Icons.Default.LocationOn, detail.location)
                InfoRow(Icons.Default.Work, detail.jobType)
                InfoRow(Icons.Default.MonetizationOn, detail.salaryRange)
            }
            Divider()
        }

        item {
            SectionTitle(title = "Deskripsi Pekerjaan")
            Text(
                text = detail.description ?: "Tidak ada deskripsi",
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 24.sp,
                color = Color.DarkGray
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(title = "Persyaratan")
            Text(
                text = detail.requirements ?: "-",
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 24.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Icon(icon, contentDescription = null, tint = SecondaryGreen)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.Black)
    }
}