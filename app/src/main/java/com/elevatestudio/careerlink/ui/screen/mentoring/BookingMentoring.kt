package com.elevatestudio.careerlink.ui.screen.mentoring

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingMentoringScreen(
    navController: NavController,
    sessionId: String? = null
) {
    var showSuccessDialog by remember { mutableStateOf(false) }

    var nama by remember { mutableStateOf("") }
    var tanggalLahir by remember { mutableStateOf("") }
    var jenisKelamin by remember { mutableStateOf("") }
    var pendidikan by remember { mutableStateOf("") }
    var programStudi by remember { mutableStateOf("") }
    var nomorAktif by remember { mutableStateOf("") }
    var harapan by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Booking Mentoring") },
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Harap Lengkapi Data Diri Anda",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LabeledTextField(
                label = "Nama Lengkap",
                value = nama,
                onValueChange = { nama = it }
            )

            LabeledTextField(
                label = "Tanggal Lahir",
                value = tanggalLahir,
                onValueChange = { tanggalLahir = it }
            )

            LabeledTextField(
                label = "Jenis Kelamin",
                value = jenisKelamin,
                onValueChange = { jenisKelamin = it }
            )

            LabeledTextField(
                label = "Pendidikan",
                value = pendidikan,
                onValueChange = { pendidikan = it }
            )

            LabeledTextField(
                label = "Program Studi",
                value = programStudi,
                onValueChange = { programStudi = it }
            )

            // Nomor Aktif +62
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Nomor Aktif",
                    modifier = Modifier.width(120.dp),
                    style = MaterialTheme.typography.bodyLarge
                )

                Text("+62", color = Color.Gray)
                Spacer(Modifier.width(8.dp))

                OutlinedTextField(
                    value = nomorAktif,
                    onValueChange = { nomorAktif = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "Harapan Tentang Mentoring ini",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = harapan,
                onValueChange = { harapan = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp),
                minLines = 5
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    // TODO: simpan booking ke backend
                    showSuccessDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Book Now", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Berhasil") },
            text = { Text("Anda telah berhasil mendaftar sesi mentoring") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false

                        val sid = sessionId ?: return@TextButton

                        navController.navigate("catatan_mentoring/$sid") {
                            popUpTo("booking_mentoring/$sid") { inclusive = true }
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            modifier = Modifier.width(120.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
    }
}
