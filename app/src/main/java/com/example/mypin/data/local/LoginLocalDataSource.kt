package com.example.mypin.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "login_prefs")

class LoginLocalDataSource(private val context: Context) {

    companion object {
        private val KEY_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        const val DEMO_EMAIL = "nutty@gmail.com"
        const val DEMO_PASSWORD = "123456"
    }

    suspend fun isLoggedIn(): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[KEY_LOGGED_IN] ?: false
    }

    suspend fun saveLoginState(loggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = loggedIn
        }
    }
}
