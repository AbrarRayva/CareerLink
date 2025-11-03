package com.elevatestudio.careerlink.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elevatestudio.careerlink.ui.components.AppLogo
import com.elevatestudio.careerlink.ui.components.AuthTextField
import com.elevatestudio.careerlink.ui.components.PrimaryButton
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.TextBlack

@Composable
fun SignUpScreen(
    onNavigateToSignIn: () -> Unit,
    onSignUpClicked: (String, String, String) -> Unit // email, pass, confirmPass
) {
    // State buat nampung ketikan
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
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
            text = "Daftarkan Akun Anda",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextBlack
        )
        Spacer(modifier = Modifier.height(32.dp))

        // 3. Form Input
        AuthTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = "Alamat Email",
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuthTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = "Kata Sandi",
            isPassword = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        AuthTextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = "Konfirmasi Kata Sandi",
            isPassword = true
        )
        Spacer(modifier = Modifier.height(32.dp))

        // 4. Tombol Daftar
        PrimaryButton(
            text = "Daftar",
            onClick = { onSignUpClicked(email.value, password.value, confirmPassword.value) }
        )

        // 5. Tombol buat yang udah punya akun
        Spacer(modifier = Modifier.weight(1f)) // Dorong ke bawah
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Sudah punya akun?", color = TextBlack)
            TextButton(onClick = onNavigateToSignIn) {
                Text(text = "Masuk di sini", color = PrimaryGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}