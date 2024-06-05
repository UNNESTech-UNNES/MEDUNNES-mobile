package com.medunnes.telemedicine.data.api

import com.medunnes.telemedicine.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/users")
    fun getAllUsers(
        @Query("page") page: String
    ) : Call<UserResponse>
}