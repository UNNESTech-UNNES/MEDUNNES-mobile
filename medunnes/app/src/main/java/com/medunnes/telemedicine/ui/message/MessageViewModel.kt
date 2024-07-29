package com.medunnes.telemedicine.ui.message

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.CatatanDataItem
import com.medunnes.telemedicine.data.response.CatatanResponse
import com.medunnes.telemedicine.data.response.DataItem
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.data.response.DosenDataItem
import com.medunnes.telemedicine.data.response.KonsultasiDataItem
import com.medunnes.telemedicine.data.response.KonsultasiResponse
import com.medunnes.telemedicine.data.response.PasienDataItem
import kotlinx.coroutines.launch

class MessageViewModel(private val repository: UserRepository): ViewModel() {
    private val _user = MutableLiveData<List<DataItem>>()
    val user: LiveData<List<DataItem>> get() = _user

    private val _pasien = MutableLiveData<List<PasienDataItem>>()
    val pasien: LiveData<List<PasienDataItem>> get() = _pasien

    private val _dokter = MutableLiveData<List<DokterDataItem>>()
    val dokter: LiveData<List<DokterDataItem>> get() = _dokter
    private val _dosen = MutableLiveData<List<DosenDataItem>>()
    val dosen: LiveData<List<DosenDataItem>> get() = _dosen

    private val _konsultasi = MutableLiveData<List<KonsultasiDataItem>>()
    val konsultasi: LiveData<List<KonsultasiDataItem>> get() = _konsultasi

    private val _catatan = MutableLiveData<List<CatatanDataItem>>()
    val catatan: LiveData<List<CatatanDataItem>> get() = _catatan

    fun getUserLogin(userId: Int) {
        viewModelScope.launch {
            try {
                val userData = repository.getUserLogin(userId)
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
                val pasienData = repository.getPasienByUser(userId)
                if (pasienData.data.isNotEmpty()) {
                    _pasien.value = pasienData.data
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }

    fun getPasienrById(id: Int) {
        viewModelScope.launch {
            try {
                val pasien = repository.getPasienById(id)
                if (pasien.data.isNotEmpty()) {
                    _pasien.value = pasien.data
                }
            } catch (e: Exception) {
                Log.d("ERROR PASIEN", e.message.toString())
            }
        }
    }

    fun getDokterById(id: Int) {
        viewModelScope.launch {
            try {
                val dokterData = repository.getDokterById(id)
                if (dokterData.data.isNotEmpty()) {
                    _dokter.value = dokterData.data
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
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }

    fun getDosenByIdDosen(id: Int) {
        viewModelScope.launch {
            try {
                val dosen = repository.getDosenByIdDosen(id)
                if (dosen.data.isNotEmpty()) {
                    _dosen.value = dosen.data
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }

    fun getCatatanByKonsultasiId(id: Int) {
        viewModelScope.launch {
            val catatanData = repository.getCatatanByKonsultasiId(id)
            try {
                _catatan.value = catatanData.data
            } catch (e: Exception) {
                _catatan.value = catatanData.data
                Log.d("ERROR", e.toString())
            }
        }
    }

    fun getKonsultasiById(id: Int) {
        viewModelScope.launch {
            try {
                val konsultasiData = repository.getKonsultasiById(id)
                _konsultasi.value = konsultasiData.data
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }

    suspend fun insertCatatan(
        konsultasiId: Long,
        gejala: String,
        diagnosis: String,
        catatan: String
    ): CatatanResponse = repository.insertCatatan(konsultasiId, gejala, diagnosis, catatan)

    suspend fun updateCatatan(
        id: Int,
        konsultasiId: Long,
        gejala: String,
        diagnosis: String,
        catatan: String
    ): CatatanResponse = repository.updateCatatan(id, konsultasiId, gejala, diagnosis, catatan)

    suspend fun updateKonsultasi(
        id: Int,
        pasienId: Long,
        dokterId: Long,
        topik: String,
        status: String
    ): KonsultasiResponse = repository.updateKonsultasi(id, pasienId, dokterId, topik, status)

    suspend fun getUserLoginId() = repository.getUserId()
    suspend fun getUserRole() = repository.getUserRole()

}