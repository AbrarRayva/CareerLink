// Lokasi: ui/screen/kursus/DaftarKursusScreen.kt
package com.elevatestudio.careerlink.ui.screen.kursus

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.elevatestudio.careerlink.data.model.KursusItem
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarKursusScreen(
    onBackClick: () -> Unit,
    onKursusClick: (String) -> Unit
) {
    val viewModel: KursusViewModel = viewModel()
    val listState by viewModel.listKursusState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.getAllCourses()
    }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = {
                    // --- PERBAIKAN SEARCH BAR ---
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.getAllCourses(searchQuery)
                        },
                        placeholder = { Text("Cari kursus...", fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true,
                        // PERBAIKAN: Hapus contentPadding (penyebab error)
                        // PERBAIKAN: Hapus .height(50.dp) agar teks tidak kepotong
                        textStyle = TextStyle(fontSize = 14.sp), // Kecilkan font biar rapi
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp), // Beri jarak sedikit dari kanan
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            viewModel.getAllCourses(searchQuery)
                            focusManager.clearFocus()
                        })
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (listState) {
                is KursusUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PrimaryGreen)
                }
                is KursusUiState.Error -> {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Gagal memuat data", color = Color.Red)
                        Button(onClick = { viewModel.getAllCourses() }) { Text("Coba Lagi") }
                    }
                }
                is KursusUiState.Success -> {
                    val data = (listState as KursusUiState.Success).data
                    if (data.isEmpty()) {
                        Text("Tidak ada kursus ditemukan", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
                        ) {
                            itemsIndexed(data) { index, kursus ->
                                KursusListCard(item = kursus, onClick = { onKursusClick(kursus.id.toString()) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KursusListCard(item: KursusItem, onClick: () -> Unit) {
    val fallbackImage = "https://picsum.photos/seed/${item.id}/200/200"

    val fullUrl = if (!item.imageUrl.isNullOrEmpty() && !item.imageUrl.startsWith("http")) {
        "${ApiClient.BASE_URL}${item.imageUrl}"
    } else if (!item.imageUrl.isNullOrEmpty()) {
        item.imageUrl
    } else {
        fallbackImage
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = fullUrl,
                contentDescription = item.title,
                placeholder = ColorPainter(Color.LightGray),
                error = ColorPainter(Color.Gray),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 80.dp, height = 80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.providerName, style = MaterialTheme.typography.labelMedium, color = PrimaryGreen)
                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 2)
                Text(item.locationType, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Icon(Icons.Default.PlayArrow, contentDescription = "Lihat", tint = PrimaryGreen)
        }
    }
}