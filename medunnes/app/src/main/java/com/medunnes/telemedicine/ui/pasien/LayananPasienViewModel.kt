package com.medunnes.telemedicine.ui.pasien

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.model.JanjiDanPasien
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.data.response.DokterResponse
import kotlinx.coroutines.launch

class LayananPasienViewModel(private val repository: UserRepository) : ViewModel() {
    private val _dokter = MutableLiveData<List<DokterDataItem>>()
    val dokter: LiveData<List<DokterDataItem>> get() = _dokter
    fun getAllDokter(): LiveData<List<UserAndDokter>> = repository.getAllDokter()
    fun getDokterByUid(uid: Int): LiveData<List<UserAndDokter>> = repository.getUserAndDokter(uid)
    fun getDokterBySpeciality(speciality: Int): LiveData<List<UserAndDokter>> = repository.getDokterBySpeciality(speciality)
    fun getUserProfile(uid: Int): LiveData<List<User>> = repository.getUser(uid)
    fun insertJanjiPasien(janji: Janji) = repository.insertJanjiPasien(janji)
    fun getDokterByJanji(uid: Int): LiveData<List<JanjiDanPasien>> = repository.getDokterByJanji(uid)
    fun getDokterByDokterId(dokterId: Int): LiveData<List<UserAndDokter>> = repository.getDokterByDokterId(dokterId)
    fun getPasienByUser(uid: Int): LiveData<List<Pasien>> = repository.getPasiebByUser(uid)
    fun getPasienById(pasienId: Int): LiveData<List<Pasien>> =  repository.getPasienById(pasienId)
    fun deletePasien(pasien: Pasien) = repository.deletePasien(pasien)
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
}