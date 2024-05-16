package com.medunnes.telemedicine.ui.pasien

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.model.JanjiDanPasien
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.data.model.UserAndPasien
import com.medunnes.telemedicine.data.repository.UserRepository

class LayananPasienViewModel(private val repository: UserRepository) : ViewModel() {
    fun getAllDokter(): LiveData<List<UserAndDokter>> = repository.getAllDokter()
    fun getDokterByUid(uid: Int): LiveData<List<UserAndDokter>> = repository.getUserAndDokter(uid)
    fun getDokterBySpeciality(speciality: String): LiveData<List<UserAndDokter>> = repository.getDokterBySpeciality(speciality)
    fun getUserProfile(uid: Int): LiveData<List<User>> = repository.getUser(uid)
    fun insertJanjiPasien(janji: Janji) = repository.insertJanjiPasien(janji)
    fun getDokterByJanji(uid: Int): LiveData<List<JanjiDanPasien>> = repository.getDokterByJanji(uid)
    fun getDokterByDokterId(dokterId: Int): LiveData<List<UserAndDokter>> = repository.getDokterByDokterId(dokterId)
    fun getPasienByUser(uid: Int): LiveData<List<Pasien>> = repository.getPasiebByUser(uid)
    fun insertPasien(pasien: Pasien) = repository.insertPasien(pasien)
    suspend fun getUserLoginId(): Int = repository.getUserId()
}