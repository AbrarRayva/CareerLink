package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.elevatestudio.careerlink.data.model.LowonganItem
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.ui.components.AppBottomNavBar
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarLowonganScreen(
    onLowonganClick: (String) -> Unit,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: DaftarLowonganViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val activeFilter = viewModel.currentFilter

    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    var userToken by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            val prefs = UserPreferences(context)
            userToken = prefs.authToken.first()
            viewModel.getJobs(token = userToken)
        }
    }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = { Text("CareerLink Jobs", fontWeight = FontWeight.Bold, color = Color.White) },
                actions = {
                    IconButton(onClick = { onNavigate("notifikasi") }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifikasi",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { onNavigate("riwayat_lamaran") }) {
                        Icon(Icons.Default.History, contentDescription = "Riwayat", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        },
        bottomBar = {
            AppBottomNavBar(
                currentRoute = "lowongan",
                onItemSelected = { route ->
                    if (route != "lowongan") {
                        onNavigate(route)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari posisi...") },
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.getJobs(filterType = activeFilter, searchQuery = searchQuery, token = userToken)
                        focusManager.clearFocus()
                    }) { Icon(Icons.Default.Search, null) }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.getJobs(filterType = activeFilter, searchQuery = searchQuery, token = userToken)
                    focusManager.clearFocus()
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp)
            ) {
                Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFE8F5E9), modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Default.FilterList, null, modifier = Modifier.padding(8.dp), tint = PrimaryGreen)
                }
                Spacer(modifier = Modifier.width(8.dp))

                FilterChipItem(text = "Semua", isSelected = activeFilter == null) {
                    viewModel.getJobs(filterType = null, searchQuery = searchQuery, token = userToken)
                }

                val filters = listOf("Full Time", "Internship", "Part Time", "Contract")
                filters.forEach { filter ->
                    FilterChipItem(text = filter, isSelected = activeFilter == filter) {
                        viewModel.getJobs(filterType = filter, searchQuery = searchQuery, token = userToken)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                when (uiState) {
                    is HomeUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PrimaryGreen)
                    is HomeUiState.Error -> {
                        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Gagal memuat data", color = Color.Gray)
                            Button(onClick = { viewModel.getJobs(activeFilter, searchQuery, userToken) }) { Text("Coba Lagi") }
                        }
                    }
                    is HomeUiState.Success -> {
                        val jobs = (uiState as HomeUiState.Success).jobs
                        if (jobs.isEmpty()) {
                            Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Work, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                                Text("Lowongan tidak ditemukan / Sudah dilamar.", color = Color.Gray)
                            }
                        } else {
                            LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                                items(jobs) { job ->
                                    LowonganCard(item = job, onClick = { onLowonganClick(job.id.toString()) })
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) PrimaryGreen else Color.White,
        contentColor = if (isSelected) Color.White else PrimaryGreen,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, if (isSelected) PrimaryGreen else Color.LightGray),
        modifier = Modifier.padding(end = 8.dp).height(40.dp).clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
fun LowonganCard(item: LowonganItem, onClick: () -> Unit) {
    val fullLogoUrl = if (item.logoUrl != null && !item.logoUrl.startsWith("http")) "${ApiClient.BASE_URL}${item.logoUrl}" else item.logoUrl

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                AsyncImage(
                    model = fullLogoUrl, contentDescription = null,
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF5F5F5)).padding(4.dp),
                    contentScale = ContentScale.Crop,
                    fallback = rememberVectorPainter(Icons.Default.Apartment),
                    error = rememberVectorPainter(Icons.Default.Apartment)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(item.companyName, fontSize = 13.sp, color = Color.Gray)
                }
                Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(4.dp)) {
                    Text(item.jobType, fontSize = 10.sp, color = PrimaryGreen, modifier = Modifier.padding(6.dp, 2.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp)); HorizontalDivider(color = Color(0xFFEEEEEE)); Spacer(modifier = Modifier.height(12.dp))

            InfoRowItem(Icons.Default.LocationOn, item.location)
            Spacer(modifier = Modifier.height(6.dp))
            InfoRowItem(Icons.Default.MonetizationOn, item.salaryRange)
            Spacer(modifier = Modifier.height(6.dp))
            InfoRowItem(Icons.Default.AccessTime, item.duration ?: "Tidak disebutkan")
            Spacer(modifier = Modifier.height(6.dp))
            InfoRowItem(Icons.Default.School, item.semester ?: "Minimal Semester 3")
        }
    }
}

@Composable
fun InfoRowItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, Modifier.size(14.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 13.sp, color = Color.DarkGray, fontWeight = FontWeight.Normal)
    }
}