package com.medunnes.telemedicine.ui.pasien

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.data.response.JanjiResponse
import com.medunnes.telemedicine.data.response.KonsultasiDataItem
import com.medunnes.telemedicine.data.response.PasienDataItem
import com.medunnes.telemedicine.data.response.PasienTambahanDataItem
import com.medunnes.telemedicine.data.response.PasienTambahanResponse
import com.medunnes.telemedicine.data.response.SesiDataItem
import kotlinx.coroutines.launch

class LayananPasienViewModel(private val repository: UserRepository) : ViewModel() {
    private val _dokter = MutableLiveData<List<DokterDataItem>>()
    val dokter: LiveData<List<DokterDataItem>> get() = _dokter

    private val _pasien = MutableLiveData<List<PasienDataItem>>()
    val pasien: LiveData<List<PasienDataItem>> get() = _pasien

    private val _pasienTambahan = MutableLiveData<List<PasienTambahanDataItem>>()
    val pasienTambahan: LiveData<List<PasienTambahanDataItem>> get() = _pasienTambahan

    private val _sesi = MutableLiveData<List<SesiDataItem>>()
    val sesi: LiveData<List<SesiDataItem>> get() = _sesi

    private val _konsultasi = MutableLiveData<List<KonsultasiDataItem>>()
    val konsultasi: LiveData<List<KonsultasiDataItem>> get() = _konsultasi
    suspend fun getUserLoginId(): Int = repository.getUserId()
    suspend fun getUserRole(): Int = repository.getUserRole()
    fun getAllDokter(page: Int) {
        viewModelScope.launch {
            try {
                val dokterData = repository.getAllDokter(page)
                if (dokterData.data.isNotEmpty()) {
                    _dokter.value = dokterData.data
                } else {
                    _dokter.value = dokterData.data
                    Log.d("DATA DOKTER", "Data dokter kosong")
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

    fun getAllSesi(dokterId: Int) {
        viewModelScope.launch {
            try {
                val sesi = repository.getAllSesi(dokterId)
                if (sesi.data.isNotEmpty()) {
                    _sesi.value = sesi.data
                } else {
                    Log.d("DATA sesi", "Data pasien sesi")
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }

    fun getDokterById(id: Int) {
        viewModelScope.launch {
            try {
                val dokterData = repository.getDokterById(id)
                if (dokterData.data.isNotEmpty()) {
                    _dokter.value = dokterData.data
                } else {
                    Log.d("DATA DOKTER", "Data dokter kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }

    fun getPasienTambahanByPasien(id: Int) {
        viewModelScope.launch {
            try {
                val pasienTambahanData = repository.getPasienTambahanByPasien(id)
                if (pasienTambahanData.data.isNotEmpty()) {
                    _pasienTambahan.value = pasienTambahanData.data
                } else {
                    Log.d("DATA PASIEN", "Data pasien kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.message.toString())
            }
        }
    }

    fun getPasienTambahanById(pasienId: Int, id: Int) {
        viewModelScope.launch {
            try {
                val pasienTambahanData = repository.getPasienTambahanById(pasienId, id)
                if (pasienTambahanData.data.isNotEmpty()) {
                    _pasienTambahan.value = pasienTambahanData.data
                } else {
                    Log.d("DATA PASIEN", "Data pasien kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.message.toString())
            }
        }
    }

    suspend fun insertJanji(
        pasienId: Long,
        dokterId: Long,
        pasien_tambahanId: Long,
        sesiId: Long,
        jadwal: String,
        catatan: String,
        status: String
    ): JanjiResponse = repository.insertJanji(
        pasienId, dokterId, pasien_tambahanId, sesiId, jadwal, catatan, status
    )

    suspend fun insertPasienTambahan(
        pasienId: Long,
        namaPasienTambahan: String,
        tb: Int,
        bb: Int,
        jenisKelamin: String,
        hubunganKeluarga: String
    ): PasienTambahanResponse = repository.insertPasienTambahan(
        pasienId, namaPasienTambahan, tb, bb, jenisKelamin, hubunganKeluarga
    )

    suspend fun updatePasienTambahan(
        id: Int,
        pasienId: Long,
        namaPasienTambahan: String,
        tb: Int,
        bb: Int,
        jenisKelamin: String,
        hubunganKeluarga: String
    ): PasienTambahanResponse = repository.updatePasienTambahan(
        id, pasienId, namaPasienTambahan, tb, bb, jenisKelamin, hubunganKeluarga
    )

    suspend fun deletePasien(id: Int): PasienTambahanResponse = repository.deletePasien(id)

    fun getKonsultasiByPasienId(id: Int) {
        viewModelScope.launch {
            try {
                val konsultasiData = repository.getKonsultasiByPasienId(id)
                if (!konsultasiData.data.isNullOrEmpty()) {
                    _konsultasi.value = konsultasiData.data
                } else {
                    _konsultasi.value = konsultasiData.data
                    Log.d("DATA DOKTER", "Data dokter kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR KONS", e.message.toString())
            }
        }
    }
}