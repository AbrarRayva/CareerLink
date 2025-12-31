package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel

import com.elevatestudio.careerlink.data.model.NotifikasiItem
import com.elevatestudio.careerlink.data.remote.ApiClient
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NotifikasiViewModel : ViewModel() {
    var notifikasiList by mutableStateOf<List<NotifikasiItem>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)

    fun loadNotif(token: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = ApiClient.instance.getNotifikasi("Bearer $token")
                if (response.isSuccessful) {
                    notifikasiList = response.body() ?: emptyList()
                }
            } catch (e: Exception) { }
            finally { isLoading = false }
        }
    }

    fun hapus(token: String, id: String) {
        viewModelScope.launch {
            try {
                ApiClient.instance.hapusNotifikasi("Bearer $token", id)
                loadNotif(token)
            } catch (e: Exception) { }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifikasiScreen(
    onBackClick: () -> Unit,
    viewModel: NotifikasiViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var userToken by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        scope.launch {
            userToken = UserPreferences(context).authToken.first() ?: ""
            if (userToken.isNotEmpty()) viewModel.loadNotif(userToken)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifikasi") },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Kembali") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PrimaryGreen)
            } else if (viewModel.notifikasiList.isEmpty()) {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Notifications, null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                    Text("Belum ada notifikasi", color = Color.Gray)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    items(viewModel.notifikasiList) { item ->
                        NotifItemCard(item, onDelete = {
                            if (userToken.isNotEmpty()) viewModel.hapus(userToken, item.id)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun NotifItemCard(item: NotifikasiItem, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, style = MaterialTheme.typography.titleMedium)
                Text(item.message, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Text(item.createdAt ?: "-", style = MaterialTheme.typography.labelSmall, color = PrimaryGreen)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
            }
        }
    }
}