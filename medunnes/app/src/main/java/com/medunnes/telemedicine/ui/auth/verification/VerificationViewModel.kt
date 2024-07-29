package com.medunnes.telemedicine.ui.auth.verification

import androidx.lifecycle.ViewModel
import com.medunnes.telemedicine.data.repository.UserRepository
import com.medunnes.telemedicine.data.response.UserResponse

class VerificationViewModel(private val repository: UserRepository): ViewModel() {
    suspend fun verifyEmail(
        id: Int,
        token: String
    ): UserResponse = repository.verifyEmail(id, token)

    suspend fun getUserId(): Int = repository.getUserId()
}