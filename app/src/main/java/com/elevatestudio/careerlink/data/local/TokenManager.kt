package com.elevatestudio.careerlink.data.local

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "careerlink_prefs"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER_FULL_NAME = "user_full_name"
        private const val KEY_USER_EMAIL = "user_email"
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveUserData(fullName: String?, email: String?) {
        prefs.edit().apply {
            fullName?.let { putString(KEY_USER_FULL_NAME, it) }
            email?.let { putString(KEY_USER_EMAIL, it) }
            apply()
        }
    }

    fun getUserFullName(): String? {
        return prefs.getString(KEY_USER_FULL_NAME, null)
    }

    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    fun clearToken() {
        prefs.edit().apply {
            remove(KEY_TOKEN)
            remove(KEY_USER_FULL_NAME)
            remove(KEY_USER_EMAIL)
            apply()
        }
    }

    fun hasToken(): Boolean {
        return getToken() != null
    }
}

