package com.medunnes.telemedicine.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.repository.UserRepository

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserProfile(uid: Int): LiveData<List<User>> = userRepository.getUser(uid)
    suspend fun getUserLoginId(): Int = userRepository.getUserId()
    suspend fun setLogoutStatus() = userRepository.setLogoutStatus()
}