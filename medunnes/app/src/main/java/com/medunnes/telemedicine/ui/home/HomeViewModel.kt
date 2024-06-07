package com.medunnes.telemedicine.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.PasienResponse
import com.medunnes.telemedicine.data.response.UserResponse

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Fitur belum tersedia"
    }
    val text: LiveData<String> = _text

    fun getUser(uid: Int): LiveData<List<User>> = userRepository.getUser(uid)
    suspend fun getUserStatus(): Boolean = userRepository.getLoginStatus()
    suspend fun getUserLoginId(): Int = userRepository.getUserId()
    suspend fun getUserRole(): Int = userRepository.getUserRole()
    suspend fun getAllUser(page: String): UserResponse = userRepository.getAllUser(page)
    suspend fun getUserLogin(id: Int): UserResponse = userRepository.getUserLogin(id)
    suspend fun getPasienByUser(userId: Int): PasienResponse = userRepository.getPasienByUser(userId)
    fun getPasienByUserId(userId: Int): LiveData<List<Pasien>> = userRepository.getPasiebByUser(userId)
}