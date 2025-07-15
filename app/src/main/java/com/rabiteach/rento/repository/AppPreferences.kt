package com.rabiteach.rento.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rabiteach.rento.model.UserRole
import kotlinx.coroutines.flow.first

object AppPreferences {
    private val Context.dataStore by preferencesDataStore(name = "app_prefs")

    val ACCESS_CODE_KEY = stringPreferencesKey("access_code")
    val USER_ROLE_KEY = stringPreferencesKey("user_role")

    suspend fun saveAccessCode(context: Context, code: String, role: UserRole) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_CODE_KEY] = code
            prefs[USER_ROLE_KEY] = role.name
        }
    }

    suspend fun getStoredRole(context: Context): UserRole? {
        val prefs = context.dataStore.data.first()
        val roleStr = prefs[USER_ROLE_KEY]
        return roleStr?.let { UserRole.valueOf(it) }
    }

    suspend fun isAccessCodeStored(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[ACCESS_CODE_KEY] != null
    }
    suspend fun clearUserData(context: Context) {
        context.dataStore.edit {
            it.clear()
        }
    }

}