package com.elevatestudio.careerlink.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevatestudio.careerlink.data.model.AuthRequest
import com.elevatestudio.careerlink.data.model.AuthResponse
import com.elevatestudio.careerlink.data.remote.ApiClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val _authMessage = MutableStateFlow<String?>(null)
    val authMessage = _authMessage.asStateFlow()

    fun register(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.register(AuthRequest(username, password))
                if (response.isSuccessful) {
                    val body = response.body()
                    _authMessage.value = body?.message ?: "Registrasi berhasil"
                } else {
                    _authMessage.value = response.errorBody()?.string() ?: "Registrasi gagal"
                }
            } catch (e: Exception) {
                _authMessage.value = "Gagal konek ke server: ${e.localizedMessage}"
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.instance.login(AuthRequest(username, password))
                if (response.isSuccessful) {
                    val body: AuthResponse? = response.body()
                    _authMessage.value = body?.message ?: "Login berhasil"
                } else {
                    _authMessage.value = response.errorBody()?.string() ?: "Login gagal"
                }
            } catch (e: Exception) {
                _authMessage.value = "Gagal konek ke server: ${e.localizedMessage}"
            }
        }
    }
}
