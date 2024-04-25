package com.medunnes.telemedicine.ui.dokter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.repository.UserRepository

class LayananDokterViewModel(private val repository: UserRepository) : ViewModel() {
    fun getJanji(): LiveData<List<Janji>> = repository.getJanji()
}