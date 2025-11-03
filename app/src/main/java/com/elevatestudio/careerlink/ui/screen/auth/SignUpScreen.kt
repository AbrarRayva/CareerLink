package com.elevatestudio.careerlink.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elevatestudio.careerlink.ui.components.AppLogo
import com.elevatestudio.careerlink.ui.components.AuthTextField
import com.elevatestudio.careerlink.ui.components.PrimaryButton
import com.elevatestudio.careerlink.ui.theme.AppBackground
import com.elevatestudio.careerlink.ui.theme.PrimaryGreen
import com.elevatestudio.careerlink.ui.theme.TextBlack
import com.elevatestudio.careerlink.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    onNavigateToSignIn: () -> Unit,
    onSignUpClicked: (String, String, String) -> Unit // email, pass, confirmPass
) {
    val viewModel: AuthViewModel = viewModel()

    // State buat nampung ketikan
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    // State untuk snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var message by remember { mutableStateOf<String?>(null) }

    // Observasi hasil register dari ViewModel
    val authMessage by viewModel.authMessage.collectAsState()

    // Jika message berubah, tampilkan snackbar otomatis
    LaunchedEffect(authMessage) {
        authMessage?.let {
            message = it
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
            }
        }
    }

    // Scaffold agar snackbar bisa muncul
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .padding(paddingValues),
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
                onClick = {
                    when {
                        email.value.isBlank() || password.value.isBlank() || confirmPassword.value.isBlank() -> {
                            message = "Semua kolom wajib diisi"
                        }
                        password.value.length < 6 -> {
                            message = "Kata sandi minimal 6 karakter"
                        }
                        password.value != confirmPassword.value -> {
                            message = "Kata sandi dan konfirmasi tidak cocok"
                        }
                        else -> {
                            message = "Sedang memproses pendaftaran..."
                            // Panggil ViewModel (email dikirim sebagai username)
                            viewModel.register(email.value, password.value)
                            onSignUpClicked(email.value, password.value, confirmPassword.value)
                        }
                    }
                }
            )

            // Tampilkan pesan teks kecil di bawah tombol
            message?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = if (it.contains("gagal", true) || it.contains("tidak", true))
                        MaterialTheme.colorScheme.error
                    else PrimaryGreen,
                    fontSize = 14.sp
                )
            }

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
}