package com.medunnes.telemedicine.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.medunnes.telemedicine.data.datastore.AuthDataStore
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.room.dao.UserDao
import com.medunnes.telemedicine.data.room.database.UserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository private constructor(
    private val mUserDao: UserDao,
    private val application: Application,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor(),
    private val authDataStore: AuthDataStore
){

    fun getUser(userId: Int): LiveData<List<User>> = mUserDao.getUser(userId)

    fun register(user: User) {
        executorService.execute { mUserDao.insertUser(user) }
    }

    fun login(email: String, password: String): LiveData<List<User>> = mUserDao.loginUser(email, password)

    suspend fun setLoginStatus() = authDataStore.loginUser()
    suspend fun getLoginStatus(): Boolean = authDataStore.isLogin()
    suspend fun setLogoutStatus() = authDataStore.logoutUser()
    suspend fun setUserId(uid: Int) = authDataStore.setUserId(uid)
    suspend fun getUserId() = authDataStore.getUserId()

    companion object {
        @Volatile
        private var instances: UserRepository? = null

        fun getInstance(
            mUserDao: UserDao,
            application: Application,
            executorService: ExecutorService,
            authDataStore: AuthDataStore
        ): UserRepository = instances ?: synchronized(this) {
            instances ?: UserRepository(mUserDao, application, executorService, authDataStore)
                .also { instances = it }
        }
    }
}