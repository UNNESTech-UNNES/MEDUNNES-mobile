package com.medunnes.telemedicine.ui.pasien

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medunnes.telemedicine.data.model.JanjiDanPasien
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.data.response.JanjiResponse
import com.medunnes.telemedicine.data.response.PasienDataItem
import com.medunnes.telemedicine.data.response.PasienTambahanDataItem
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
    fun getDokterByUid(uid: Int): LiveData<List<UserAndDokter>> = repository.getUserAndDokter(uid)
    fun getDokterBySpeciality(speciality: Int): LiveData<List<UserAndDokter>> = repository.getDokterBySpeciality(speciality)
    fun getUserProfile(uid: Int): LiveData<List<User>> = repository.getUser(uid)
    fun getDokterByJanji(uid: Int): LiveData<List<JanjiDanPasien>> = repository.getDokterByJanji(uid)
    fun getDokterByDokterId(dokterId: Int): LiveData<List<UserAndDokter>> = repository.getDokterByDokterId(dokterId)
    fun getPasienById(pasienId: Int): LiveData<List<Pasien>> =  repository.getPasienById(pasienId)
    suspend fun getUserLoginId(): Int = repository.getUserId()
    fun getAllDokter(page: Int) {
        viewModelScope.launch {
            try {
                val dokterData = repository.getAllDokter(page)
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

    fun getPasienByUserLogin(userId: Int) {
        viewModelScope.launch {
            try {
                val pasienData = repository.getPasienByUser(userId)
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

    fun getAllSesi() {
        viewModelScope.launch {
            try {
                val sesi = repository.getAllSesi()
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
                val dokterData = repository.getDokterByUser(id)
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
}