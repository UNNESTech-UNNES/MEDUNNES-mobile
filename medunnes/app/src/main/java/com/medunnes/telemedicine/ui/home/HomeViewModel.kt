package com.medunnes.telemedicine.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.DataItem
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.data.response.PasienDataItem
import com.medunnes.telemedicine.data.response.UserResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Fitur belum tersedia"
    }
    val text: LiveData<String> = _text

    private val _user = MutableLiveData<List<DataItem>>()
    val user: LiveData<List<DataItem>> get() = _user

    private val _pasien = MutableLiveData<List<PasienDataItem>>()
    val pasien: LiveData<List<PasienDataItem>> get() = _pasien

    private val _dokter = MutableLiveData<List<DokterDataItem>>()
    val dokter: LiveData<List<DokterDataItem>> get() = _dokter

    suspend fun getUserStatus(): Boolean = userRepository.getLoginStatus()
    suspend fun getUserLoginId(): Int = userRepository.getUserId()
    suspend fun getUserRole(): Int = userRepository.getUserRole()
    suspend fun getAllUser(page: String): UserResponse = userRepository.getAllUser(page)
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
}