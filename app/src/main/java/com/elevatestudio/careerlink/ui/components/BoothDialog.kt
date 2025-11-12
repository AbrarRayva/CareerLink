package com.elevatestudio.careerlink.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen
import com.elevatestudio.careerlink.data.model.CareerFairModels.Booth

@Composable
fun BoothDialog(
    booth: Booth,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(booth.company, fontWeight = FontWeight.Bold, color = PrimaryGreen) },
        text = { Text(booth.desc, color = Color.DarkGray) },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)) {
                Text("Lihat Booth", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup", color = SecondaryGreen)
            }
        }
    )
}
