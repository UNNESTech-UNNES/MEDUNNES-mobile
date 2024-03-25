package com.medunnes.telemedicine

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.di.Injection
import com.medunnes.telemedicine.ui.auth.login.LoginViewModel
import com.medunnes.telemedicine.ui.auth.register.RegisterViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when(modelClass) {

        RegisterViewModel::class.java -> RegisterViewModel(repository)
        LoginViewModel::class.java -> LoginViewModel(repository)

        else -> throw IllegalArgumentException("Uknown view model class: " + modelClass.name)
    } as T

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context) : ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}