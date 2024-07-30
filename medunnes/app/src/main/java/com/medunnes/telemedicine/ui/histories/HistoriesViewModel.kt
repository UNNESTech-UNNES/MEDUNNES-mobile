package com.medunnes.telemedicine.ui.histories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.data.response.DosenDataItem
import com.medunnes.telemedicine.data.response.KonsultasiDataItem
import com.medunnes.telemedicine.data.response.PasienDataItem
import kotlinx.coroutines.launch

class HistoriesViewModel(val repository: UserRepository) : ViewModel() {
    private val _konsultasi = MutableLiveData<List<KonsultasiDataItem>>()
    val konsultasi: LiveData<List<KonsultasiDataItem>> get() = _konsultasi
    private val _pasien = MutableLiveData<List<PasienDataItem>>()
    val pasien: LiveData<List<PasienDataItem>> get() = _pasien
    private val _dokter = MutableLiveData<List<DokterDataItem>>()
    val dokter: LiveData<List<DokterDataItem>> get() = _dokter
    private val _dosen = MutableLiveData<List<DosenDataItem>>()
    val dosen: LiveData<List<DosenDataItem>> get() = _dosen

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

    fun getKonsultasiByDokterId(id: Int) {
        viewModelScope.launch {
            try {
                val konsultasiData = repository.getKonsultasiByDokterId(id)
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

    fun getDokterByUserId(id: Int) {
        viewModelScope.launch {
            try {
                val dokter = repository.getDokterByUser(id)
                if (!dokter.data.isNullOrEmpty()) {
                    _dokter.value = dokter.data
                } else {
                    Log.d("DATA DOKTER", "Data dokter kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR DOKTER", e.message.toString())
            }
        }
    }

    fun getDosenById(id: Int) {
        viewModelScope.launch {
            try {
                val dosenData = repository.getDosenById(id)
                if (dosenData.data.isNotEmpty()) {
                    _dosen.value = dosenData.data
                } else {
                    Log.d("DATA DOKTER", "Data dosen kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }

    fun getDokterByDosen(id: Int) {
        viewModelScope.launch {
            try {
                val dokterData = repository.getDokterByDosen(id)
                if (dokterData.data.isNotEmpty()) {
                    _dokter.value = dokterData.data
                } else {
                    Log.d("DATA DOKTER", "Data mahasiwa kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }

    fun getKonsultasiByDokter(id: Int) {
        viewModelScope.launch {
            try {
                val konsultasi = repository.getKonsultasiByDokterId(id)
                if (konsultasi.data.isNotEmpty()) {
                    _konsultasi.value = konsultasi.data
                } else {
                    _konsultasi.value = konsultasi.data
                    Log.d("DATA DOKTER", "Data dokter kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR DOKTER", e.message.toString())
            }
        }
    }

    suspend fun getUserLoginId(): Int = repository.getUserId()
    suspend fun getUserRole(): Int = repository.getUserRole()
}