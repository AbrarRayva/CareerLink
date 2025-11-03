package com.elevatestudio.careerlink.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.elevatestudio.careerlink.R // Pastikan import R-nya benar

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_careerlink_logo),
        contentDescription = "CareerLink Logo",
        modifier = modifier.size(80.dp)
    )
}