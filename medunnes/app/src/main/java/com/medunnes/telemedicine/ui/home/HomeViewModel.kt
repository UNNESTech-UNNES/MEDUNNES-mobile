package com.medunnes.telemedicine.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.repository.UserRepository

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Fitur belum tersedia"
    }
    val text: LiveData<String> = _text

    fun getUser(uid: Int): LiveData<List<User>> = userRepository.getUser(uid)
    suspend fun getUserStatus(): Boolean = userRepository.getLoginStatus()
    suspend fun getUserLoginId(): Int = userRepository.getUserId()
    suspend fun getUserRole(): Int = userRepository.getUserRole()
}