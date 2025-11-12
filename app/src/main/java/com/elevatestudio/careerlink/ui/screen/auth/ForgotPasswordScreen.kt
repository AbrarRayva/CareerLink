package com.elevatestudio.careerlink.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elevatestudio.careerlink.ui.components.AppLogo
import com.elevatestudio.careerlink.ui.components.AuthTextField
import com.elevatestudio.careerlink.ui.components.PrimaryButton
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.TextBlack

@Composable
fun ForgotPasswordScreen(
    onSavePasswordClicked: (String, String, String) -> Unit
) {
    val email = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Logo
        AppLogo()
        Spacer(modifier = Modifier.height(24.dp))

        // 2. Judul
        Text(
            text = "Ganti Kata Sandi Anda",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextBlack
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 3. Deskripsi
        Text(
            text = "Masukkan email dan kata sandi baru Anda.",
            fontSize = 14.sp,
            color = TextBlack.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // 4. Form Input
        AuthTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = "Alamat Email",
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(16.dp)) // Tambah input password baru
        AuthTextField(
            value = newPassword.value,
            onValueChange = { newPassword.value = it },
            label = "Kata Sandi Baru",
            isPassword = true
        )
        Spacer(modifier = Modifier.height(16.dp)) // Tambah input konfirmasi
        AuthTextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = "Konfirmasi Kata Sandi Baru",
            isPassword = true
        )
        Spacer(modifier = Modifier.height(32.dp))


        // 5. Tombol Kirim
        PrimaryButton(
            text = "Simpan Kata Sandi",
            onClick = { onSavePasswordClicked(email.value, newPassword.value, confirmPassword.value) },
        )
    }
}