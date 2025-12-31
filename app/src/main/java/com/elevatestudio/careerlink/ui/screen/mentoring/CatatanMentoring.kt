package com.elevatestudio.careerlink.ui.screen.mentoring

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.Icons
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
fun CatatanMentoringScreen(navController: NavController) {
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
           
            Text("Catatan", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

           
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
               
                BasicTextField(
                    value = catatan,
                    onValueChange = { catatan = it },
                    modifier = Modifier.fillMaxSize(),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    decorationBox = { innerTextField ->
                        if (catatan.isEmpty()) {
                            Text("Tulis catatan Anda di sini...", color = Color.Gray.copy(alpha = 0.6f))
                        }
                        innerTextField()
                    }
                )

               
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(Icons.Filled.Attachment, contentDescription = "Lampiran", modifier = Modifier.size(24.dp).padding(end = 16.dp))
                    Icon(Icons.Filled.Edit, contentDescription = "Gambar", modifier = Modifier.size(24.dp).padding(end = 16.dp))
                    Icon(Icons.Filled.Image, contentDescription = "Gambar", modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}