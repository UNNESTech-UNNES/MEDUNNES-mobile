package com.medunnes.telemedicine.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medunnes.telemedicine.data.api.ApiConfig
import com.medunnes.telemedicine.data.model.Dokter
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.data.model.UserAndPasien
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.DataItem
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.data.response.DokterResponse
import com.medunnes.telemedicine.data.response.PasienDataItem
import com.medunnes.telemedicine.data.response.PasienResponse
import com.medunnes.telemedicine.data.response.UserResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<List<DataItem>>()
    val user: LiveData<List<DataItem>> get() = _user

    private val _pasien = MutableLiveData<List<PasienDataItem>>()
    val pasien: LiveData<List<PasienDataItem>> get() = _pasien

    private val _dokter = MutableLiveData<List<DokterDataItem>>()
    val dokter: LiveData<List<DokterDataItem>> get() = _dokter


    fun getUserProfile(uid: Int): LiveData<List<User>> = userRepository.getUser(uid)
    fun updateUserProfile(user: User) = userRepository.updateProfile(user)
    fun getUserAndDokter(uid: Int): LiveData<List<UserAndDokter>> = userRepository.getUserAndDokter(uid)
    fun getPasienByUser(userId: Int): LiveData<List<Pasien>> = userRepository.getPasienById(userId)
    suspend fun getUserLoginId(): Int = userRepository.getUserId()
    suspend fun setLogoutStatus() = userRepository.setLogoutStatus()
    suspend fun getLoginStatus(): Boolean = userRepository.getLoginStatus()
    suspend fun getUserRole(): Int = userRepository.getUserRole()
    suspend fun getUserProfileData(id: Int): UserResponse = userRepository.getUserLogin(id)
    suspend fun getUserPasienProfile(id: Int): PasienResponse = userRepository.getPasienByUser(id)

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
        nama: String,
        img: String? = null,
        kelamin: String,
        alamat: String,
        noTlp: String,
        tb: Int,
        bb: Int,
        status: String
    ): PasienResponse = userRepository.updatePasien(
        id, userId, nik, nama, img, kelamin, alamat, noTlp, tb, bb, status
    )

    suspend fun updateDokter(
        id: Long,
        userId: Long,
        spesialisId: Long,
        titleDepan: String,
        nama: String,
        titleBelakang: String,
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
        id, userId, spesialisId, titleDepan, nama, titleBelakang, img, alamat, noTlp,
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