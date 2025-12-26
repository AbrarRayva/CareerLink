package com.elevatestudio.careerlink.ui.screen.mentoring

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatatanMentoringScreen(navController: NavController, sessionId: String? = null) {
    var catatan by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catatan Mentoring") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Catatan",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            // Container catatan
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            ) {
                // Area input (kasih padding bottom supaya tidak ketutup ikon)
                BasicTextField(
                    value = catatan,
                    onValueChange = { catatan = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .padding(bottom = 48.dp), // ✅ ruang untuk ikon footer
                    textStyle = MaterialTheme.typography.bodyLarge,
                    decorationBox = { innerTextField ->
                        if (catatan.isBlank()) {
                            Text(
                                "Tulis catatan Anda di sini...",
                                color = Color.Gray.copy(alpha = 0.6f)
                            )
                        }
                        innerTextField()
                    }
                )

                // Footer ikon (fixed di bawah)
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Attachment,
                        contentDescription = "Lampiran",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = "Gambar",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Optional: tombol simpan (kalau kamu mau)
            Button(
                onClick = {
                    // TODO: simpan catatan ke backend pakai sessionId
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan Catatan")
            }
        }
    }
}
