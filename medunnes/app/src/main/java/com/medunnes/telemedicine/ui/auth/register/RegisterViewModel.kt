package com.medunnes.telemedicine.ui.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _data = MutableLiveData<String>()
    val data: LiveData<String> = _data

    fun setData(newData: String) {
        _data.value = newData
    }

    fun getUser(userId: Int): LiveData<List<User>> = userRepository.getUser(userId)

    fun register(user: User) {
        userRepository.register(user)
    }

    fun posData(data: String) {
        _data.postValue(data)
    }
}