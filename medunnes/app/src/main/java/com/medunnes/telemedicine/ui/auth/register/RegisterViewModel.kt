package com.medunnes.telemedicine.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.model.Dokter
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.PasienResponse
import com.medunnes.telemedicine.data.response.UserResponse

class RegisterViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _data = MutableLiveData<String>()
    val data: LiveData<String> = _data
    fun getUser(userId: Int): LiveData<List<User>> = userRepository.getUser(userId)
    fun register(user: User): Long =  userRepository.register(user)
    fun registerDokter(dokter: Dokter): Long = userRepository.registerDokter(dokter)
    fun insertPasien(pasien: Pasien) = userRepository.insertPasien(pasien)
    suspend fun registerAPI(
        name: String,
        email: String,
        password: String,
        type: String
    ): UserResponse = userRepository.register(
        name, email, password, type
    )
    suspend fun insertPasien(
        userId: Long,
        nik: Long,
        nama: String,
        img: String? = null,
        kelamin: String,
        alamat: String,
        noTlp: String,
        tb: Int,
        bb: Int,
        status: String
    ): PasienResponse = userRepository.insertPasien(
        userId, nik, nama, img, kelamin, alamat, noTlp, tb, bb, status
    )
    fun isEmailExist(email: String): LiveData<List<User>> = userRepository.isEmailExist(email)
}