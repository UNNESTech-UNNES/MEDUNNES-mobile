package com.medunnes.telemedicine

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.di.Injection
import com.medunnes.telemedicine.ui.auth.login.LoginViewModel
import com.medunnes.telemedicine.ui.auth.register.RegisterViewModel
import com.medunnes.telemedicine.ui.auth.verification.VerificationViewModel
import com.medunnes.telemedicine.ui.dokter.LayananDokterViewModel
import com.medunnes.telemedicine.ui.dosen.LayananDosenViewModel
import com.medunnes.telemedicine.ui.histories.HistoriesViewModel
import com.medunnes.telemedicine.ui.home.HomeViewModel
import com.medunnes.telemedicine.ui.message.MessageViewModel
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import com.medunnes.telemedicine.ui.profile.ProfileViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when(modelClass) {

        RegisterViewModel::class.java -> RegisterViewModel(repository)
        LoginViewModel::class.java -> LoginViewModel(repository)
        HomeViewModel::class.java -> HomeViewModel(repository)
        ProfileViewModel::class.java -> ProfileViewModel(repository)
        LayananPasienViewModel::class.java -> LayananPasienViewModel(repository)
        LayananDokterViewModel::class.java -> LayananDokterViewModel(repository)
        LayananDosenViewModel::class.java -> LayananDosenViewModel(repository)
        MessageViewModel::class.java -> MessageViewModel(repository)
        HistoriesViewModel::class.java -> HistoriesViewModel(repository)
        VerificationViewModel::class.java -> VerificationViewModel(repository)

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