// Lokasi: ui/components/AppDialogs.kt
package com.elevatestudio.careerlink.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info // <-- Ini buat ikon caution 'i'
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen // <-- Import ini buat tombol NO

/**
 * Dialog konfirmasi "Yes/No"
 * Dipakai untuk: Terapkan Filter, Kirim Lamaran
 */
@Composable
fun ConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    icon: @Composable (() -> Unit)? = null // Parameter ikon tetap ada
) {
    // KITA PAKAI DIALOG BIAR BISA FULL KUSTOMISASI
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Ikon Caution (sesuai request)
                icon?.invoke() // Panggil ikon yang dikirim dari layar

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Judul
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 3. Tombol Kiri-Kanan (SESUAI REQUEST)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally) // Atur jarak & posisi
                ) {
                    // Tombol "NO" (kiri)
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f), // Bagi rata
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SecondaryGreen
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(SecondaryGreen)
                        )
                    ) {
                        Text("NO")
                    }

                    // Tombol "YES" (kanan)
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        modifier = Modifier.weight(1f) // Bagi rata
                    ) {
                        Text("YES")
                    }
                }
            }
        }
    }
}

/**
 * Dialog custom untuk "Lamaran Berhasil Terkirim"
 */
@Composable
fun SuccessDialog(
    onDismiss: () -> Unit,
    onGoToHome: () -> Unit
) {
    // Dialog ini sudah benar
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = PrimaryGreen,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Lamaran berhasil terkirim!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Perusahaan akan segera memeriksa lamaranmu, goodluck!",
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))
                PrimaryButton(
                    text = "BACK TO HOME",
                    onClick = {
                        onDismiss() // Tutup dialog
                        onGoToHome() // Navigasi
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}