package com.elevatestudio.careerlink.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.elevatestudio.careerlink.ui.viewmodel.AuthViewModel
import com.elevatestudio.careerlink.ui.viewmodel.AuthState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onNavigateToSignIn: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    val viewModel: AuthViewModel = viewModel()
    val authState by viewModel.authState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    // --- LOGIKA (Dilarang ada UI di sini) ---
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar("Pendaftaran Berhasil! Silakan Masuk.")
                }
                viewModel.resetState()
                onSignUpSuccess()
            }
            is AuthState.Error -> {
                val errorMsg = (authState as AuthState.Error).message
                scope.launch {
                    snackbarHostState.showSnackbar(errorMsg)
                }
                viewModel.resetState()
            }
            else -> {}
        }
    }

    // --- TAMPILAN (UI) ---
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppLogo()
            Spacer(modifier = Modifier.height(24.dp))
            Text("Buat Akun Baru", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))

            AuthTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = "Nama Lengkap"
            )
            Spacer(modifier = Modifier.height(16.dp))
            AuthTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = "Email",
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(16.dp))
            AuthTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = "Kata Sandi",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = if (authState is AuthState.Loading) "Memproses..." else "Daftar",
                onClick = {
                    if (name.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty()) {
                        viewModel.register(name.value, email.value, password.value)
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Harap isi semua kolom!")
                        }
                    }
                },
                enabled = authState !is AuthState.Loading
            )

            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Sudah punya akun?", color = TextBlack)
                TextButton(onClick = onNavigateToSignIn) {
                    Text(text = "Masuk", color = PrimaryGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}