package com.elevatestudio.careerlink.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.elevatestudio.careerlink.data.local.TokenManager
import com.elevatestudio.careerlink.data.model.AuthRequest
import com.elevatestudio.careerlink.data.model.RegisterRequest
import com.elevatestudio.careerlink.data.remote.ApiClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val tokenManager = TokenManager(getApplication())

    private val _authMessage = MutableStateFlow<String?>(null)
    val authMessage = _authMessage.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        // Check if user is already logged in
        _isLoggedIn.value = tokenManager.hasToken()
    }

    fun register(fullName: String, email: String, password: String, role: String = "student") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.instance.register(RegisterRequest(fullName, email, password, role))
                if (response.isSuccessful && response.body()?.success == true) {
                    val message = response.body()?.message ?: "Registrasi berhasil"
                    _authMessage.value = message
                } else {
                    _authMessage.value = response.body()?.message ?: "Registrasi gagal"
                }
            } catch (e: Exception) {
                _authMessage.value = "Gagal konek ke server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.instance.login(AuthRequest(email, password))
                if (response.isSuccessful && response.body()?.success == true) {
                    val userData = response.body()?.data
                    val token = userData?.token
                    
                    if (token != null) {
                        // Simpan token dan user data
                        tokenManager.saveToken(token)
                        tokenManager.saveUserData(userData?.fullName, userData?.email)
                        _isLoggedIn.value = true
                        _authMessage.value = response.body()?.message ?: "Login berhasil"
                    } else {
                        _authMessage.value = "Token tidak diterima dari server"
                    }
                } else {
                    _authMessage.value = response.body()?.message ?: "Login gagal"
                }
            } catch (e: Exception) {
                _authMessage.value = "Gagal konek ke server: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        tokenManager.clearToken()
        _isLoggedIn.value = false
    }

    fun getUserFullName(): String? {
        return tokenManager.getUserFullName()
    }
}
