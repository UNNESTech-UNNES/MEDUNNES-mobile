package com.medunnes.telemedicine.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.DataItem
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.data.response.DokterResponse
import com.medunnes.telemedicine.data.response.PasienDataItem
import com.medunnes.telemedicine.data.response.PasienResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<List<DataItem>>()
    val user: LiveData<List<DataItem>> get() = _user

    private val _pasien = MutableLiveData<List<PasienDataItem>>()
    val pasien: LiveData<List<PasienDataItem>> get() = _pasien

    private val _dokter = MutableLiveData<List<DokterDataItem>>()
    val dokter: LiveData<List<DokterDataItem>> get() = _dokter

    suspend fun getUserLoginId(): Int = userRepository.getUserId()
    suspend fun setLogoutStatus() = userRepository.setLogoutStatus()
    suspend fun getLoginStatus(): Boolean = userRepository.getLoginStatus()
    suspend fun getUserRole(): Int = userRepository.getUserRole()

    fun getUserLogin(userId: Int) {
        viewModelScope.launch {
            try {
                val userData = userRepository.getUserLogin(userId)
                if (userData.data.isNotEmpty()) {
                    _user.value = userData.data
                } else {
                    Log.d("DATA USER", "Data user kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }

    fun getPasienByUserLogin(userId: Int) {
        viewModelScope.launch {
            try {
                val pasienData = userRepository.getPasienByUser(userId)
                if (pasienData.data.isNotEmpty()) {
                    _pasien.value = pasienData.data
                } else {
                    Log.d("DATA pasien", "Data pasien kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }

    fun getDokterByUserLogin(userId: Int) {
        viewModelScope.launch {
            try {
                val dokterData = userRepository.getDokterByUser(userId)
                if (dokterData.data.isNotEmpty()) {
                    _dokter.value = dokterData.data
                } else {
                    Log.d("DATA pasien", "Data pasien kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }

    suspend fun updatePasien(
        id: Int,
        userId: Long,
        nik: Long,
        img: String? = null,
        kelamin: String,
        alamat: String,
        noTlp: String,
        tb: Int,
        bb: Int,
        status: String
    ): PasienResponse = userRepository.updatePasien(
        id, userId, nik, img, kelamin, alamat, noTlp, tb, bb, status
    )

    suspend fun updateDokter(
        id: Long,
        userId: Long,
        spesialisId: Long,
        img: String? = null,
        alamat: String,
        noTlp: String,
        tempatKerja: String,
        tahunLulus: Int,
        tglAktif: String,
        alumni: String,
        noReg: Long,
        jenisKelamin: String,
        status: String
    ): DokterResponse = userRepository.updateDokter(
        id, userId, spesialisId, img, alamat, noTlp,
        tempatKerja, tahunLulus, tglAktif, alumni, noReg, jenisKelamin,status
    )

    suspend fun uploadImagePasien(
        id: Int,
        multipartBody: MultipartBody.Part
    ): PasienResponse = userRepository.uploadImagePasien(id, multipartBody)

    suspend fun uploadImageDokter(
        id: Int,
        multipartBody: MultipartBody.Part
    ): PasienResponse = userRepository.uploadImageDokter(id, multipartBody)
}