package com.elevatestudio.careerlink.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
    }

    val authToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[TOKEN_KEY]
    }

    val userName: Flow<String?> = context.dataStore.data.map { it[USER_NAME] }

    suspend fun saveAuthToken(token: String, userId: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_ID_KEY] = userId
        }
    }
    suspend fun clearAuthToken() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { it[USER_NAME] = name }
    }
}