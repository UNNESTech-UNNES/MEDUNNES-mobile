package com.medunnes.telemedicine.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences

class AuthDataStore constructor(private val context: Context) {
    private val Context.datastore by preferencesDataStore(name = "auth_datastore")

    suspend fun loginUser() {
        val dataStoreKey = booleanPreferencesKey("auth_datastore")
        context.datastore.edit {
            it[dataStoreKey] = true
        }
    }

    suspend fun isLogin(): Boolean {
        val dataStoreKey = booleanPreferencesKey("auth_datastore")
        val status = context.datastore.data.first()

        return status[dataStoreKey] ?: false
    }

    suspend fun setUserId(uid: Int) {
        val userId = intPreferencesKey("auth_user_id")
        context.datastore.edit {
            it[userId] = uid
        }
    }

    suspend fun getUserId(): Int {
        val userId = intPreferencesKey("auth_user_id")
        val idUser = context.datastore.data.first()
        return idUser[userId] ?: 0
    }

    suspend fun logoutUser() {
        val dataStoreKey = booleanPreferencesKey("auth_datastore")
        context.datastore.edit {
            it[dataStoreKey] = false
        }
    }

}