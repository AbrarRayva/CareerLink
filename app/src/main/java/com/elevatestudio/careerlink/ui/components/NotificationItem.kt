package com.elevatestudio.careerlink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen

@Composable
fun NotificationItem(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SecondaryGreen, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(message, color = Color.White, fontWeight = FontWeight.Medium)
    }
}
