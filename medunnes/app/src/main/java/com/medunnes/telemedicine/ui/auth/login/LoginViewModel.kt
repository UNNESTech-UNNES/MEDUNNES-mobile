package com.medunnes.telemedicine.ui.auth.login

import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.LoginResponse
import com.medunnes.telemedicine.data.response.UserResponse

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    suspend fun login(email: String, password: String): LoginResponse = userRepository.login(email, password)
    suspend fun setLoginStatus() = userRepository.setLoginStatus()
    suspend fun setUserLoginId(uid: Int) = userRepository.setUserId(uid)
    suspend fun setUserLoginRole(role: Int) = userRepository.setUserRole(role)

    suspend fun getUserStatus(): Boolean = userRepository.getLoginStatus()
    suspend fun getUserLogin(id: Int): UserResponse = userRepository.getUserLogin(id)



}