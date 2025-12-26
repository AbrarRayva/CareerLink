// Lokasi: ui/screen/auth/SignInScreen.kt
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
import com.elevatestudio.careerlink.ui.viewmodel.AuthState
import com.elevatestudio.careerlink.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay // WAJIB IMPORT INI
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onSignInSuccess: () -> Unit
) {
    val viewModel: AuthViewModel = viewModel()
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    // --- LOGIC BARU: PESAN DULU, BARU PINDAH ---
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                // 1. Tampilkan Pesan Login Sukses
                launch {
                    snackbarHostState.showSnackbar("Login berhasil! Selamat datang.")
                }

                // 2. Tunggu 1.5 detik
                delay(1500)

                // 3. Pindah ke Home
                onSignInSuccess()
                viewModel.resetState()
            }
            is AuthState.Error -> {
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
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppLogo()
            Spacer(modifier = Modifier.height(24.dp))
            Text("Masuk Ke Akun Anda", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))

            AuthTextField(value = email.value, onValueChange = { email.value = it }, label = "Email", keyboardType = KeyboardType.Email)
            Spacer(modifier = Modifier.height(16.dp))
            AuthTextField(value = password.value, onValueChange = { password.value = it }, label = "Kata Sandi", isPassword = true)

            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onNavigateToForgotPassword) {
                    Text(text = "Lupa Password?", color = PrimaryGreen)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = if (authState is AuthState.Loading) "Sedang Masuk..." else "Masuk",
                onClick = { viewModel.login(email.value, password.value, context) },
                enabled = authState !is AuthState.Loading
            )

            Spacer(modifier = Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Belum punya akun?", color = TextBlack)
                TextButton(onClick = onNavigateToSignUp) {
                    Text(text = "Daftar di sini", color = PrimaryGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}