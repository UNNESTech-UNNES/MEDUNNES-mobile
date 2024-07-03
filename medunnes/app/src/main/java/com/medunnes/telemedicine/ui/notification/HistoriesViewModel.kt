package com.medunnes.telemedicine.ui.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.KonsultasiDataItem
import com.medunnes.telemedicine.data.response.PasienDataItem
import kotlinx.coroutines.launch

class HistoriesViewModel(val repository: UserRepository) : ViewModel() {
    private val _konsultasi = MutableLiveData<List<KonsultasiDataItem>>()
    val konsultasi: LiveData<List<KonsultasiDataItem>> get() = _konsultasi
    private val _pasien = MutableLiveData<List<PasienDataItem>>()
    val pasien: LiveData<List<PasienDataItem>> get() = _pasien

    fun getKonsultasiByPasienId(id: Int) {
        viewModelScope.launch {
            try {
                val konsultasiData = repository.getKonsultasiByPasienId(id)
                if (!konsultasiData.data.isNullOrEmpty()) {
                    _konsultasi.value = konsultasiData.data
                } else {
                    _konsultasi.value = konsultasiData.data
                    Log.d("DATA KONSULTASI", "Data konsultasi kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR KONS", e.message.toString())
            }
        }
    }

    fun getPasienByUserLogin(userId: Int) {
        viewModelScope.launch {
            try {
                val pasienData = repository.getPasienByUser(userId)
                if (!pasienData.data.isNullOrEmpty()) {
                    _pasien.value = pasienData.data
                } else {
                    Log.d("DATA pasien", "Data pasien kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR PP", e.message.toString())
            }
        }
    }

    suspend fun getUserLoginId(): Int = repository.getUserId()
}