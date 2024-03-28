package com.medunnes.telemedicine.ui.pasien

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.data.repository.UserRepository

class LayananPasienViewModel(private val repository: UserRepository) : ViewModel() {
    fun getAllDokter(): LiveData<List<UserAndDokter>> = repository.getAllDokter()
}