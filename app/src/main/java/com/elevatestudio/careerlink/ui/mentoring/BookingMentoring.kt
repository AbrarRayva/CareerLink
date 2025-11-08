package com.elevatestudio.careerlink.ui.mentoring

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookingMentoringScreen(navController: NavController) { // Parameter diubah menjadi _sessionId
    // State untuk setiap input field
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
                        // PERBAIKAN: Menggunakan ikon AutoMirrored
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Harap Lengkapi Data Diri Anda", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 16.dp))

            // Pemanggilan fungsi Composable yang sudah dipindahkan keluar
            FormField(label = "Nama Lengkap", value = nama, onValueChange = { nama = it })
            FormField(label = "Tanggal Lahir", value = tanggalLahir, onValueChange = { tanggalLahir = it })
            FormField(label = "Jenis Kelamin", value = jenisKelamin, onValueChange = { jenisKelamin = it })
            FormField(label = "Pendidikan", value = pendidikan, onValueChange = { pendidikan = it })
            FormField(label = "Program Studi", value = programStudi, onValueChange = { programStudi = it })

            // Input Nomor Aktif dengan Prefix +62
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Nomor Aktif", modifier = Modifier.width(120.dp), style = MaterialTheme.typography.bodyLarge)
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                    Text("+62", modifier = Modifier.padding(end = 4.dp), style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray))
                    OutlinedTextField(
                        value = nomorAktif,
                        onValueChange = { nomorAktif = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Harapan Tentang Mentoring ini", modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), style = MaterialTheme.typography.bodyLarge)
            OutlinedTextField(
                value = harapan,
                onValueChange = { harapan = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp),
                minLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            Spacer(Modifier.height(32.dp))

            // Tombol "Book Now"
            Button(
                onClick = { /* Lakukan validasi dan proses booking */ },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Book Now", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

// PERBAIKAN: Fungsi Helper Composable dipindahkan keluar dari BookingMentoringScreen
@Composable
fun FormField(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.width(120.dp), style = MaterialTheme.typography.bodyLarge)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}
