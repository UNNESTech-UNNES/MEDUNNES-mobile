package com.medunnes.telemedicine.data.api

import com.medunnes.telemedicine.data.response.LoginResponse
import com.medunnes.telemedicine.data.response.PasienResponse
import com.medunnes.telemedicine.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/users")
    suspend fun getAllUsers(
        @Query("page") page: String
    ) : UserResponse

    @GET("api/users/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ) : UserResponse

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @GET("api/pasien")
    suspend fun getAllPasien(
        @Query("page") page: String
    ) : PasienResponse

    @GET("api/pasien/{id}")
    suspend fun getPasienByUser(
        @Path("id") id: Int
    ) : PasienResponse
}