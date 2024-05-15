package com.medunnes.telemedicine.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.model.Dokter
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _data = MutableLiveData<String>()
    val data: LiveData<String> = _data
    fun getUser(userId: Int): LiveData<List<User>> = userRepository.getUser(userId)
    fun register(user: User): Long =  userRepository.register(user)
    fun registerDokter(dokter: Dokter) = userRepository.registerDokter(dokter)
    fun insertPasien(pasien: Pasien) = userRepository.insertPasien(pasien)
    fun isEmailExist(email: String): LiveData<List<User>> = userRepository.isEmailExist(email)
}