package com.medunnes.telemedicine.di

import android.app.Application
import android.content.Context
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.room.dao.UserDao
import com.medunnes.telemedicine.data.room.database.UserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = UserDatabase.getDatabase(context)
        val userDao = database.userDao()
        val application = Application()
        val executorService = Executors.newSingleThreadScheduledExecutor()
        return UserRepository.getInstance(userDao, application, executorService)
    }
}