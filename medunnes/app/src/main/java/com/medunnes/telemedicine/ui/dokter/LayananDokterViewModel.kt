package com.medunnes.telemedicine.ui.dokter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.model.JanjiDanPasien
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.data.repository.UserRepository

class LayananDokterViewModel(private val repository: UserRepository) : ViewModel() {
    fun getJanji(): LiveData<List<JanjiDanPasien>> = repository.getJanji()
    fun getJanjiAndPasien(dokter_id: Int): LiveData<List<JanjiDanPasien>> = repository.getJanjiAndPasien(dokter_id)
    fun getUserAndDokterId(uid: Int): LiveData<List<UserAndDokter>> = repository.getUserAndDokter(uid)
    fun updateJanjiPasien(janji: Janji) = repository.updateJanjiPasien(janji)
    suspend fun getUserLogin() = repository.getUserId()
}