package com.medunnes.telemedicine.ui.dosen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.data.response.DosenDataItem
import com.medunnes.telemedicine.data.response.KonsultasiDataItem
import kotlinx.coroutines.launch

class LayananDosenViewModel(private val repository: UserRepository): ViewModel() {
    private val _dosen = MutableLiveData<List<DosenDataItem>>()
    val dosen: LiveData<List<DosenDataItem>> get() = _dosen
    private val _mahasiswa = MutableLiveData<List<DokterDataItem>>()
    val mahasiswa: LiveData<List<DokterDataItem>> get() = _mahasiswa
    private val _konsultasi = MutableLiveData<List<KonsultasiDataItem>>()
    val konsultasi: LiveData<List<KonsultasiDataItem>> get() = _konsultasi
    suspend fun getUserLoginId(): Int = repository.getUserId()

    fun getDokterByDosen(id: Int) {
        viewModelScope.launch {
            try {
                val dokterData = repository.getDokterByDosen(id)
                if (dokterData.data.isNotEmpty()) {
                    _mahasiswa.value = dokterData.data
                } else {
                    Log.d("DATA DOKTER", "Data mahasiwa kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
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

}