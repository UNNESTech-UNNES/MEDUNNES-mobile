package com.medunnes.telemedicine.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository): ViewModel() {

    fun getUser(userId: Int): LiveData<List<User>> = userRepository.getUser(userId)

    fun register(user: User) {
        userRepository.register(user)
    }
}