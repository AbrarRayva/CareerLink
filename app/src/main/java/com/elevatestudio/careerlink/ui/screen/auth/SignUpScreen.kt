// Lokasi: ui/screen/auth/SignUpScreen.kt
package com.elevatestudio.careerlink.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState // Tambah ini biar bisa discroll kalo layar kecil
import androidx.compose.foundation.verticalScroll
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
import com.elevatestudio.careerlink.ui.viewmodel.AuthState // Pastikan AuthState terimport
import com.elevatestudio.careerlink.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    onNavigateToSignIn: () -> Unit,
    onSignUpSuccess: () -> Unit // Callback navigasi kalau register berhasil
) {
    val viewModel: AuthViewModel = viewModel()

    // 1. OBSERVE STATE (Pantau status backend)
    val authState by viewModel.authState.collectAsState()

    // State Input
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    // State UI
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // 2. LOGIC NAVIGASI OTOMATIS (LaunchedEffect)
    // Kode ini jalan otomatis setiap kali 'authState' berubah
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                launch {
                    snackbarHostState.showSnackbar("Pendaftaran berhasil! Silakan login.")
                }
                delay(1500)
                onSignUpSuccess()
                viewModel.resetState()
            }
            is AuthState.Error -> {
                // Kalo gagal, munculin snackbar error dari backend
                snackbarHostState.showSnackbar((authState as AuthState.Error).message)
                viewModel.resetState()
            }
            else -> {}
        }
    }

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
            // Logo & Judul
            AppLogo()
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Daftarkan Akun Anda",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextBlack
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Form Input
            AuthTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = "Nama Lengkap",
                keyboardType = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(16.dp))

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

            // 3. TOMBOL DAFTAR
            PrimaryButton(
                // Ubah teks tombol jadi "Loading..." pas lagi loading
                text = if (authState is AuthState.Loading) "Sedang Memproses..." else "Daftar",

                // Matikan tombol pas lagi loading biar gak diklik berkali-kali
                enabled = authState !is AuthState.Loading,

                onClick = {
                    // Validasi Lokal Dulu
                    when {
                        name.value.isBlank() -> {
                            coroutineScope.launch { snackbarHostState.showSnackbar("Nama Lengkap wajib diisi") }
                        }
                        email.value.isBlank() || password.value.isBlank() || confirmPassword.value.isBlank() -> {
                            coroutineScope.launch { snackbarHostState.showSnackbar("Semua kolom wajib diisi") }
                        }
                        password.value.length < 6 -> {
                            coroutineScope.launch { snackbarHostState.showSnackbar("Kata sandi minimal 6 karakter") }
                        }
                        password.value != confirmPassword.value -> {
                            coroutineScope.launch { snackbarHostState.showSnackbar("Kata sandi tidak cocok") }
                        }
                        else -> {
                            // Panggil ViewModel
                            viewModel.register(name.value, email.value, password.value)
                        }
                    }
                },
            )

            // Navigasi ke Login
            Spacer(modifier = Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Sudah punya akun?", color = TextBlack)
                TextButton(onClick = onNavigateToSignIn) {
                    Text(text = "Masuk di sini", color = PrimaryGreen, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}