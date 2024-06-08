package com.medunnes.telemedicine.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.DataItem
import com.medunnes.telemedicine.data.response.PasienResponse
import com.medunnes.telemedicine.data.response.UserResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Fitur belum tersedia"
    }
    val text: LiveData<String> = _text

    private val _user = MutableLiveData<List<DataItem>>()
    val user: LiveData<List<DataItem>> get() = _user

    fun getUser(uid: Int): LiveData<List<User>> = userRepository.getUser(uid)
    suspend fun getUserStatus(): Boolean = userRepository.getLoginStatus()
    suspend fun getUserLoginId(): Int = userRepository.getUserId()
    suspend fun getUserRole(): Int = userRepository.getUserRole()
    suspend fun getAllUser(page: String): UserResponse = userRepository.getAllUser(page)
    fun getUserLogin(userId: Int) {
        viewModelScope.launch {
            try {
                val userData = userRepository.getUserLogin(userId)
                if (!userData.data.isNullOrEmpty()) {
                    _user.value = userData.data
                } else {
                    Log.d("DATA USER", "Data user kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }
    suspend fun getPasienByUser(userId: Int): PasienResponse = userRepository.getPasienByUser(userId)
    fun getPasienByUserId(userId: Int): LiveData<List<Pasien>> = userRepository.getPasiebByUser(userId)
}