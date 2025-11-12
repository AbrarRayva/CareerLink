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
fun SignInScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onSignInClicked: (String, String) -> Unit // Kirim email & pass buat dicek
) {
    val viewModel: AuthViewModel = viewModel()

    // Bikin state buat nampung ketikan email & password
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    // State untuk pesan feedback dan snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var message by remember { mutableStateOf<String?>(null) }

    // Observasi dari ViewModel (pesan hasil login)
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

    // Scaffold agar snackbar bisa tampil
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
            Spacer(modifier = Modifier.height(24.dp)) // Kasih jarak

            // 2. Judul
            Text(
                text = "Masuk Ke Akun Anda",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextBlack
            )
            Spacer(modifier = Modifier.height(32.dp))

            // 3. Form Input Email
            AuthTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = "Alamat Email",
                keyboardType = KeyboardType.Email // Biar keyboardnya ada '@'
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 4. Form Input Password
            AuthTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = "Kata Sandi",
                isPassword = true // Biar jadi bintang-bintang
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 5. Lupa Password (di pojok kanan)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onNavigateToForgotPassword) {
                    Text(text = "Lupa Password?", color = PrimaryGreen)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // 6. Tombol Masuk
            PrimaryButton(
                text = "Masuk",
                onClick = {
                    when {
                        email.value.isBlank() || password.value.isBlank() -> {
                            message = "Email dan password wajib diisi"
                        }
                        else -> {
                            message = "Sedang memproses login..."
                            // Panggil ViewModel (email dikirim sebagai username)
                            viewModel.login(email.value, password.value)
                            onSignInClicked(email.value, password.value)
                        }
                    }
                },
            )

            // Tampilkan pesan teks kecil di bawah tombol
            message?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it,
                    color = if (it.contains("gagal", true) || it.contains("salah", true))
                        MaterialTheme.colorScheme.error
                    else PrimaryGreen,
                    fontSize = 14.sp
                )
            }

            // 7. Tombol Daftar jika belum punya akun
            Spacer(modifier = Modifier.weight(1f)) // Dorong item ini ke paling bawah
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Belum punya akun?", color = TextBlack)
                TextButton(onClick = onNavigateToSignUp) {
                    Text(text = "Daftar di sini", color = PrimaryGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
