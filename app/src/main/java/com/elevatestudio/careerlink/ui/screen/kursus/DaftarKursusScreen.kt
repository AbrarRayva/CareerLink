// Lokasi: ui/screen/kursus/DaftarKursusScreen.kt
package com.elevatestudio.careerlink.ui.screen.kursus

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.elevatestudio.careerlink.data.model.KursusItem
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen

// Data dummy
val dummyKursusList = listOf(
    KursusItem("1", "UPT Kewirausahaan dan Karir Unand", "Cara Membuat CV", "Offline", "https://picsum.photos/seed/a/200"),
    KursusItem("2", "FTI Unand", "Pintar UI/UX", "Online", "https://picsum.photos/seed/b/200"),
    KursusItem("3", "UPT Kewirausahaan dan Karir Unand", "Teknik Menjadi Wirausahawan", "Online", "https://picsum.photos/seed/c/200"),
    KursusItem("4", "Simplilearn", "Latih Berpikir Kreatif", "Online", "https://picsum.photos/seed/d/200"),
    KursusItem("5", "Microsoft", "UI/UX Course", "Online", "https://picsum.photos/seed/e/200")
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DaftarKursusScreen(
    onBackClick: () -> Unit,
    onKursusClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        containerColor = AppBackground,
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)
        ) {
            itemsIndexed(dummyKursusList) { index, kursus ->
                // --- INI ANIMASI LIST ITEM ---
                AnimatedVisibility(
                    visible = true, // Selalu true, tapi 'enter' akan jalan pas pertama nampil
                    enter = fadeIn(tween(300, delayMillis = index * 50)) +
                            slideInVertically(tween(300, delayMillis = index * 50)) { it / 2 }
                ) {
                    KursusListCard(
                        item = kursus,
                        onClick = { onKursusClick(kursus.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun KursusListCard(item: KursusItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SecondaryGreen.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.judul,
                placeholder = ColorPainter(Color.LightGray),
                fallback = ColorPainter(Color.Gray),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 80.dp, height = 60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.penyelenggara, style = MaterialTheme.typography.bodySmall)
                Text(item.judul, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(item.tipe, style = MaterialTheme.typography.bodyMedium)
            }
            Icon(Icons.Default.PlayArrow, contentDescription = "Lihat", tint = PrimaryGreen)
        }
    }
}