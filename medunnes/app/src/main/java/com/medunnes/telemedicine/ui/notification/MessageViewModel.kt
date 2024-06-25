package com.medunnes.telemedicine.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MessageViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Fitur belum tersedia"
    }
    val text: LiveData<String> = _text
}