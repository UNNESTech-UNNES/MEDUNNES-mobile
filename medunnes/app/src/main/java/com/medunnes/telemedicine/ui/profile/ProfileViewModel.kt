package com.medunnes.telemedicine.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.model.Dokter
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.data.model.UserAndPasien
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.PasienResponse
import com.medunnes.telemedicine.data.response.UserResponse

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getUserProfile(uid: Int): LiveData<List<User>> = userRepository.getUser(uid)
    fun updateUserProfile(user: User) = userRepository.updateProfile(user)
    fun getUserAndDokter(uid: Int): LiveData<List<UserAndDokter>> = userRepository.getUserAndDokter(uid)
    fun getPasienByUser(userId: Int): LiveData<List<Pasien>> = userRepository.getPasienById(userId)
    fun updateDokter(dokter: Dokter) = userRepository.updateDokter(dokter)
    suspend fun getUserLoginId(): Int = userRepository.getUserId()
    suspend fun setLogoutStatus() = userRepository.setLogoutStatus()
    suspend fun getLoginStatus(): Boolean = userRepository.getLoginStatus()
    suspend fun getUserRole(): Int = userRepository.getUserRole()
    suspend fun getUserProfileData(id: Int): UserResponse = userRepository.getUserLogin(id)
    suspend fun getUserPasienProfile(id: Int): PasienResponse = userRepository.getPasienByUser(id)
}