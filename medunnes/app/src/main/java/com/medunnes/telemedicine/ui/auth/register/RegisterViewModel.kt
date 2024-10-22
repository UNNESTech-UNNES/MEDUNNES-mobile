package com.medunnes.telemedicine.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.DokterResponse
import com.medunnes.telemedicine.data.response.PasienResponse
import com.medunnes.telemedicine.data.response.PasienTambahanResponse
import com.medunnes.telemedicine.data.response.UserResponse

class RegisterViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _data = MutableLiveData<String>()
    val data: LiveData<String> = _data
    fun register(user: User): Long =  userRepository.register(user)
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
        img: String? = null,
        kelamin: String,
        alamat: String,
        noTlp: String,
        tb: Int,
        bb: Int,
        status: String
    ): PasienResponse = userRepository.insertPasien(
        userId, nik, img, kelamin, alamat, noTlp, tb, bb, status
    )

    suspend fun insertDokter(
        userId: Long,
        dosenId: Long,
        spesialisId: Long,
        img: String? = null,
        alamat: String,
        noTlp: String,
        noReg: Long,
        jenisKelamin: String,
        status: String
    ): DokterResponse = userRepository.insertDokter(
        userId, dosenId, spesialisId, img, alamat, noTlp, noReg, jenisKelamin, status
    )

    suspend fun insertPasienTambahan(
        pasienId: Long,
        namaPasienTambahan: String,
        tb: Int,
        bb: Int,
        jenisKelamin: String,
        hubunganKeluarga: String
    ): PasienTambahanResponse = userRepository.insertPasienTambahan(
        pasienId, namaPasienTambahan, tb, bb, jenisKelamin, hubunganKeluarga
    )
}