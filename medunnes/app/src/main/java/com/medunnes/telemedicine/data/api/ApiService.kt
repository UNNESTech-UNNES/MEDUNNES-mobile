package com.medunnes.telemedicine.data.api

import com.medunnes.telemedicine.data.response.ArtikelResponse
import com.medunnes.telemedicine.data.response.CatatanResponse
import com.medunnes.telemedicine.data.response.DiskusiResponse
import com.medunnes.telemedicine.data.response.DokterResponse
import com.medunnes.telemedicine.data.response.JanjiResponse
import com.medunnes.telemedicine.data.response.KonsultasiResponse
import com.medunnes.telemedicine.data.response.LoginResponse
import com.medunnes.telemedicine.data.response.PasienResponse
import com.medunnes.telemedicine.data.response.PasienTambahanResponse
import com.medunnes.telemedicine.data.response.SesiResponse
import com.medunnes.telemedicine.data.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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
        @Field("role") type: String,
        @Field("password") password: String,
    ) : UserResponse

    @FormUrlEncoded
    @PUT("api/users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Field("name") name: String,
    ) : UserResponse

    @GET("api/pasien")
    suspend fun getAllPasien() : PasienResponse

    @GET("api/pasien/{id}")
    suspend fun getPasienByUser(
        @Path("id") id: Int
    ) : PasienResponse

    @GET("api/pasien/detail/{id}")
    suspend fun getPasienById(
        @Path("id") id: Int
    ) : PasienResponse

    @FormUrlEncoded
    @POST("api/pasien")
    suspend fun insertPasien(
        @Field("id") userId: Long,
        @Field("NIK") nik: Long,
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
        @Field("id") userId: Long,
        @Field("NIK") nik: Long,
        @Field("img_pasien") img: String? = null,
        @Field("jenis_kelamin") kelamin: String,
        @Field("alamat") alamat: String,
        @Field("no_tlp") noTlp: String,
        @Field("TB") tb: Int,
        @Field("BB") bb: Int,
        @Field("status") status: String
    ) : PasienResponse

    @GET("api/dokter")
    suspend fun getAllDokter(
        @Query("page") page: Int
    ) : DokterResponse

    @GET("api/dokter/{id}")
    suspend fun getDokterByUser(
        @Path("id") id: Int
    ) : DokterResponse

    @GET("api/dokter/detail/{id}")
    suspend fun getDokterById(
        @Path("id") id: Int
    ) : DokterResponse

    @FormUrlEncoded
    @POST("api/dokter")
    suspend fun insertDokter(
        @Field("id") userId: Long,
        @Field("dosen_id") dosenId: Long,
        @Field("spesialis_id") spesialisId: Long,
        @Field("img_dokter") img: String? = null,
        @Field("alamat") alamat: String,
        @Field("no_tlp") noTlp: String,
        @Field("nim") noReg: Long,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("status") status: String
    ): DokterResponse

    @FormUrlEncoded
    @PUT("api/dokter/{id}")
    suspend fun updateDokter(
        @Path("id") id: Long,
        @Field("id") userId: Long,
        @Field("spesialis_id") spesialisId: Long,
        @Field("img_dokter") img: String? = null,
        @Field("alamat") alamat: String,
        @Field("no_tlp") noTlp: String,
        @Field("nim") noReg: Long,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("status") status: String
    ): DokterResponse

    @GET("api/pasienTambahan/{id}")
    suspend fun getPasienTambahanByPasien(
        @Path("id") id: Int
    ): PasienTambahanResponse

    @GET("api/pasienTambahan/{pasienId}/{id}")
    suspend fun getPasienTambahanById(
        @Path("pasienId") pasienId: Int,
        @Path("id") id: Int,
    ): PasienTambahanResponse

    @FormUrlEncoded
    @POST("api/pasienTambahan")
    suspend fun insertPasienTambahan(
        @Field("pasien_id") pasienId: Long,
        @Field("nama_pasien_tambahan") namaPasienTambahan: String,
        @Field("TB") tb: Int,
        @Field("BB") bb: Int,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("tgl_lahir") tglLahir: String,
        @Field("hubungan_keluarga") hubunganKeluarga: String
    ): PasienTambahanResponse

    @FormUrlEncoded
    @PUT("api/pasienTambahan/{id}")
    suspend fun updatePasienTambahan(
        @Path("id") id: Int,
        @Field("pasien_id") pasienId: Long,
        @Field("nama_pasien_tambahan") namaPasienTambahan: String,
        @Field("TB") tb: Int,
        @Field("BB") bb: Int,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("tgl_lahir") tglLahir: String,
        @Field("hubungan_keluarga") hubunganKeluarga: String
    ): PasienTambahanResponse

    @DELETE("api/pasienTambahan/{id}")
    suspend fun deletePasienTambahan(
        @Path("id") id: Int,
    ): PasienTambahanResponse

    @GET("api/janji/{id}")
    suspend fun getJanjiByDokterId(
        @Path("id") id: Int
    ): JanjiResponse

    @FormUrlEncoded
    @POST("api/janji")
    suspend fun insertJanji(
        @Field("pasien_id") pasienId: Long,
        @Field("dokter_id") dokterId: Long,
        @Field("pasien_tambahan_id") pasien_tambahanId: Long,
        @Field("sesi_id") sesiId: Long,
        @Field("datetime") jadwal: String,
        @Field("catatan") catatan: String,
        @Field("status") status: String
    ): JanjiResponse

    @FormUrlEncoded
    @PUT("api/janji/{id}")
    suspend fun updatetJanji(
        @Path("id") id: Int,
        @Field("pasien_id") pasienId: Long,
        @Field("dokter_id") dokterId: Long,
        @Field("pasien_tambahan_id") pasien_tambahanId: Long,
        @Field("sesi_id") sesiId: Long,
        @Field("datetime") jadwal: String,
        @Field("catatan") catatan: String,
        @Field("status") status: String
    ): JanjiResponse

    @GET("api/konsultasi/{id}")
    suspend fun getKonsultasiById(
        @Path("id") id: Int
    ): KonsultasiResponse

    @GET("api/konsultasi/pasien/{id}")
    suspend fun getKonsultasiByPasienId(
        @Path("id") id: Int
    ): KonsultasiResponse

    @GET("api/konsultasi/dokter/{id}")
    suspend fun getKonsultasiByDokterId(
        @Path("id") id: Int
    ): KonsultasiResponse

    @FormUrlEncoded
    @POST("api/konsultasi")
    suspend fun insertKonsultasi(
        @Field("pasien_id") pasienId: Long,
        @Field("dokter_id") dokterId: Long,
        @Field("topik") topik: String,
        @Field("status") status: String
    ): KonsultasiResponse

    @FormUrlEncoded
    @PUT("api/konsultasi/{id}")
    suspend fun updateKonsultasi(
        @Path("id") id: Int,
        @Field("pasien_id") pasienId: Long,
        @Field("dokter_id") dokterId: Long,
        @Field("topik") topik: String,
        @Field("status") status: String
    ): KonsultasiResponse

    @GET("api/sesi")
    suspend fun getAllSesi(): SesiResponse

    @Multipart
    @POST("api/pasien/uploadImage/{id}")
    suspend fun uploadImagePasien(
        @Path("id") id: Int,
        @Part file: MultipartBody.Part
    ): PasienResponse

    @Multipart
    @POST("api/dokter/uploadImage/{id}")
    suspend fun uploadImageDokter(
        @Path("id") id: Int,
        @Part file: MultipartBody.Part
    ): PasienResponse

    @FormUrlEncoded
    @POST("api/diskusi")
    suspend fun insertDiskusi(
        @Field("konsultasi_id") konsultasiId: Long,
        @Field("message") message: String
    ): DiskusiResponse

    @GET("api/catatan/{id}")
    suspend fun getCatatanByKonsultasiId(
        @Path("id") id: Int,
    ): CatatanResponse

    @FormUrlEncoded
    @POST("api/catatan")
    suspend fun insertCatatan(
        @Field("konsultasi_id") konsultasiId: Long,
        @Field("gejala") gejala: String,
        @Field("diagnosis") diagnosis: String,
        @Field("catatan") catatan: String
    ): CatatanResponse

    @FormUrlEncoded
    @PUT("api/catatan/{id}")
    suspend fun updateCatatan(
        @Path("id") id: Int,
        @Field("konsultasi_id") konsultasiId: Long,
        @Field("gejala") gejala: String,
        @Field("diagnosis") diagnosis: String,
        @Field("catatan") catatan: String
    ): CatatanResponse

    @GET("api/artikel")
    suspend fun getAllArtikel(): ArtikelResponse
}