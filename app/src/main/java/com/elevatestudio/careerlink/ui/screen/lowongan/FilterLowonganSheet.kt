// Lokasi: ui/screen/lowongan/FilterLowonganSheet.kt
package com.elevatestudio.careerlink.ui.screen.lowongan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.elevatestudio.careerlink.ui.components.ConfirmationDialog
import com.elevatestudio.careerlink.ui.components.PrimaryButton
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen

// Data dummy untuk filter
val filterCategories = listOf("Minimum Pendidikan", "Kebijakan Kerja", "Tipe Kerja", "Tingkat Semester", "Jenis Kelamin")
val filterOptions = mapOf(
    "Minimum Pendidikan" to listOf("S1", "D3"),
    "Kebijakan Kerja" to listOf("WFO", "Remote", "Hybrid"),
    "Tipe Kerja" to listOf("Part Time", "Magang", "Freelance"),
    "Tingkat Semester" to listOf("3", "5", "7"),
    "Jenis Kelamin" to listOf("Perempuan", "Laki-laki")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterLowonganSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    // State untuk tahu kategori mana yang lagi dipilih
    var selectedCategory by remember { mutableStateOf(filterCategories.first()) }

    // State untuk dialog konfirmasi
    var showTerapkanDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.85f) // Bikin sheet-nya tinggi (85% layar)
                .fillMaxWidth()
        ) {
            // Bagian Search Bar (sesuai gambar)
            OutlinedTextField(
                value = "",
                onValueChange = { },
                placeholder = { Text("Cari") },
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
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )

            // --- INI LAYOUT KIRI-KANAN ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Bikin sisanya keisi
            ) {
                // KIRI: Daftar Kategori
                LazyColumn(
                    modifier = Modifier
                        .weight(0.4f) // Lebar 40%
                        .background(Color.White)
                        .padding(top = 8.dp)
                ) {
                    items(filterCategories) { category ->
                        FilterCategoryItem( // <-- Sekarang fungsi ini ada di bawah
                            text = category,
                            isSelected = category == selectedCategory,
                            onClick = { selectedCategory = category }
                        )
                    }
                }

                // KANAN: Daftar Opsi
                LazyColumn(
                    modifier = Modifier
                        .weight(0.6f) // Lebar 60%
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Tampilkan opsi berdasarkan kategori yang dipilih
                    items(filterOptions[selectedCategory].orEmpty()) { option ->
                        FilterCheckboxItem(label = option) // <-- Sekarang fungsi ini ada di bawah
                    }
                }
            }
            // --- SELESAI LAYOUT KIRI-KANAN ---

            // Tombol Terapkan dan Reset
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White) // Kasih background biar gak transparan
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { /* TODO: Reset state */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }
                PrimaryButton(
                    text = "Terapkan",
                    onClick = {
                        // Tampilkan dialog konfirmasi
                        showTerapkanDialog = true
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

    // Tampilkan Dialog Konfirmasi
    if (showTerapkanDialog) {
        ConfirmationDialog(
            onDismiss = { showTerapkanDialog = false },
            onConfirm = {
                showTerapkanDialog = false
                onApply() // Panggil fungsi aslinya
            },
            title = "Terapkan filter ini?"
        )
    }
}

// --- INI FUNGSI YANG HILANG (Complaint 1) ---
@Composable
fun FilterCategoryItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) PrimaryGreen else Color.Transparent
    val textColor = if (isSelected) Color.White else Color.Black
    val shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)

    Text(
        text = text,
        color = textColor,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp)
    )
}

// --- INI FUNGSI YANG HILANG (Complaint 2) ---
@Composable
fun FilterCheckboxItem(label: String) {
    var isChecked by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isChecked = !isChecked }
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label)
    }
}