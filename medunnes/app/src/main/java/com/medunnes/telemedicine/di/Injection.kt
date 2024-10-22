package com.medunnes.telemedicine.di

import android.content.Context
import com.medunnes.telemedicine.data.datastore.AuthDataStore
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.room.database.UserDatabase
import java.util.concurrent.Executors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = UserDatabase.getDatabase(context)
        val userDao = database.userDao()
        val executorService = Executors.newSingleThreadScheduledExecutor()
        val authDataStore = AuthDataStore(context)
        return UserRepository.getInstance(userDao, executorService, authDataStore)
    }
}