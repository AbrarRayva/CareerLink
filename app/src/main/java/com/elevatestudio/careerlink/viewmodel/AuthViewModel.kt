package com.elevatestudio.careerlink.ui.viewmodel

import android.app.Application
import android.content.Context // Tambahan import
import android.util.Log // Tambahan import
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.elevatestudio.careerlink.data.model.AuthRequest
import com.elevatestudio.careerlink.data.remote.RetrofitClient
import com.elevatestudio.careerlink.utils.UserPreferences
import com.google.firebase.messaging.FirebaseMessaging // Tambahan import
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// State untuk UI
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // REGISTER
    fun register(name: String, email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // Sesuaikan body request dengan backend kamu
                val request = AuthRequest(email = email, password = pass, fullName = name, role = "student")
                val response = RetrofitClient.instance.register(request)

                if (response.isSuccessful) {
                    _authState.value = AuthState.Success
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Register Gagal"
                    _authState.value = AuthState.Error("Register Gagal: ${response.message()}")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Error: ${e.message}")
            }
        }
    }

    // LOGIN
    // Perhatikan: Saya menambahkan parameter 'context' agar cocok dengan SignInScreen
    fun login(email: String, pass: String, context: Context) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val request = AuthRequest(email = email, password = pass)
                val response = RetrofitClient.instance.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!

                    // 1. SIMPAN TOKEN KE HP
                    // Pastikan UserPreferences kamu support 2 parameter ini
                    userPreferences.saveAuthToken(body.token, body.user.id.toString())

                    // 2. UPDATE TOKEN FCM (Notifikasi) 🔥
                    updateFcmToken(body.token)

                    _authState.value = AuthState.Success
                } else {
                    _authState.value = AuthState.Error("Login Gagal! Cek email/password.")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Koneksi Error: ${e.message}")
            }
        }
    }

    // FUNGSI KHUSUS UPDATE FCM
    private fun updateFcmToken(authToken: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("AuthViewModel", "Gagal ambil token FCM", task.exception)
                return@addOnCompleteListener
            }

            // Ambil token dari Firebase
            val fcmToken = task.result
            Log.d("AuthViewModel", "FCM Token didapat: $fcmToken")

            // Kirim ke Backend
            viewModelScope.launch {
                try {
                    RetrofitClient.instance.updateFcmToken(
                        token = "Bearer $authToken",
                        data = mapOf("fcm_token" to fcmToken)
                    )
                    Log.d("AuthViewModel", "✅ Token FCM berhasil diupdate ke server!")
                } catch (e: Exception) {
                    Log.e("AuthViewModel", "❌ Gagal update token ke server: ${e.message}")
                }
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}