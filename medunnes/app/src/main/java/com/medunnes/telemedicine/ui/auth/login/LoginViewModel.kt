package com.medunnes.telemedicine.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.repository.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun login(email: String, password: String): LiveData<List<User>> = userRepository.login(email, password)
    suspend fun setLoginStatus() = userRepository.setLoginStatus()
    suspend fun setUserLoginId(uid: Int) = userRepository.setUserId(uid)
    suspend fun setUserLoginRole(role: Int) = userRepository.setUserRole(role)

    suspend fun getUserStatus(): Boolean = userRepository.getLoginStatus()



}