package com.elevatestudio.careerlink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elevatestudio.careerlink.ui.theme.SecondaryGreen
import com.elevatestudio.careerlink.ui.theme.TextBlack
import com.elevatestudio.careerlink.data.model.CareerFairModels.Event

@Composable
fun EventCard(event: Event, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SecondaryGreen, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(event.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextBlack)
        Text(event.date, color = Color.White)
        Text(event.location, color = Color.White)
    }
}
