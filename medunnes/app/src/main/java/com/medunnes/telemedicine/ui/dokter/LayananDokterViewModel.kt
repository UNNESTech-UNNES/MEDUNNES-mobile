package com.medunnes.telemedicine.ui.dokter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.data.response.JanjiDataItem
import com.medunnes.telemedicine.data.response.JanjiResponse
import kotlinx.coroutines.launch

class LayananDokterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _janji = MutableLiveData<List<JanjiDataItem>>()
    val janji: LiveData<List<JanjiDataItem>> get() = _janji

    private val _dokter = MutableLiveData<List<DokterDataItem>>()
    val dokter: LiveData<List<DokterDataItem>> get() = _dokter
    suspend fun getUserLogin() = repository.getUserId()

    fun getJanjiByDokterId(id: Int) {
        viewModelScope.launch {
            try {
                val janjiData = repository.getJanjiByDokterId(id)
                if (janjiData.data.isNotEmpty()) {
                    _janji.value = janjiData.data
                } else {
                    Log.d("DATA JANJI", "Data janji kosong, id: $id")
                }
            } catch (e: Exception) {
                Log.d("ERROR JANJI", e.message.toString())
            }
        }
    }

    fun getDokterByUserId(id: Int) {
        viewModelScope.launch {
            try {
                val dokter = repository.getDokterByUser(id)
                if (dokter.data.isNotEmpty()) {
                    _dokter.value = dokter.data
                } else {
                    Log.d("DATA DOKTER", "Data dokter kosong")
                }
            } catch (e: Exception) {
                Log.d("ERROR DOKTER", e.message.toString())
            }
        }
    }

    suspend fun updateJanji(
        id: Int,
        pasienId: Long,
        dokterId: Long,
        pasien_tambahanId: Long,
        sesiId: Long,
        jadwal: String,
        catatan: String,
        status: String
    ): JanjiResponse = repository.updateJanji(
        id, pasienId, dokterId, pasien_tambahanId, sesiId, jadwal, catatan, status
    )
}