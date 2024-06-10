package com.medunnes.telemedicine.data.api

import com.medunnes.telemedicine.data.response.DokterResponse
import com.medunnes.telemedicine.data.response.LoginResponse
import com.medunnes.telemedicine.data.response.PasienResponse
import com.medunnes.telemedicine.data.response.UserResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @FormUrlEncoded
    @POST("api/users")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("type") type: String,
        @Field("password") password: String,
    ) : UserResponse

    @GET("api/pasien")
    suspend fun getAllPasien(
        @Query("page") page: String
    ) : PasienResponse

    @GET("api/pasien/{id}")
    suspend fun getPasienByUser(
        @Path("id") id: Int
    ) : PasienResponse

    @FormUrlEncoded
    @POST("api/pasien")
    suspend fun insertPasien(
        @Field("user_id") userId: Long,
        @Field("nik") nik: Long,
        @Field("nama_pasien") nama: String,
        @Field("img_pasien") img: String? = null,
        @Field("jenis_kelamin") kelamin: String,
        @Field("alamat") alamat: String,
        @Field("no_tlp") noTlp: String,
        @Field("TB") tb: Int,
        @Field("BB") bb: Int,
        @Field("status") status: String
    ) : PasienResponse

    @FormUrlEncoded
    @PUT("api/pasien/{id}")
    suspend fun updatePasien(
        @Path("id") id: Int,
        @Field("user_id") userId: Long,
        @Field("NIK") nik: Long,
        @Field("nama_pasien") nama: String,
        @Field("img_pasien") img: String? = null,
        @Field("jenis_kelamin") kelamin: String,
        @Field("alamat") alamat: String,
        @Field("no_tlp") noTlp: String,
        @Field("TB") tb: Int,
        @Field("BB") bb: Int,
        @Field("status") status: String
    ) : PasienResponse

    @GET("api/dokter/{id}")
    suspend fun getDokterByUser(
        @Path("id") id: Int
    ) : DokterResponse

    @FormUrlEncoded
    @POST("api/dokter")
    suspend fun insertDokter(
        @Field("user_id") userId: Int,
        @Field("spesialis_id") spesialisId: Int,
        @Field("title_depan") titleDepan: String,
        @Field("nama_dokter") nama: String,
        @Field("title_belakang") titleBelakang: String,
        @Field("img_dokter") img: String? = null,
        @Field("alamat") alamat: String,
        @Field("no_tlp") noTlp: String,
        @Field("tempat_kerja") tempatKerja: String,
        @Field("tahun_lulus") tahunLulus: Int,
        @Field("tgl_mulai_aktif") tglAktif: String,
        @Field("alumni_kampus") alumni: String,
        @Field("no_reg") noReg: Long,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("status") status: String
    ): DokterResponse
}