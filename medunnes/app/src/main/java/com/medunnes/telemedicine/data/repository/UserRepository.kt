package com.medunnes.telemedicine.data.repository

import androidx.lifecycle.LiveData
import com.medunnes.telemedicine.data.api.ApiConfig
import com.medunnes.telemedicine.data.datastore.AuthDataStore
import com.medunnes.telemedicine.data.model.User
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
import com.medunnes.telemedicine.data.room.dao.UserDao
import okhttp3.MultipartBody
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository private constructor(
    private val mUserDao: UserDao,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor(),
    private val authDataStore: AuthDataStore
){
    // Retrofit
    // User
    suspend fun getAllUser(page: String): UserResponse = ApiConfig.getApiService().getAllUsers(page)
    suspend fun getUserLogin(id: Int): UserResponse = ApiConfig.getApiService().getUser(id)
    suspend fun login(email: String, password: String): LoginResponse = ApiConfig.getApiService().login(email, password)
    suspend fun register(
        name: String,
        email: String,
        password: String,
        type: String
    ): UserResponse = ApiConfig.getApiService().register(name, email, type, password)

    //Pasien
    suspend fun getAllPasien(): PasienResponse = ApiConfig.getApiService().getAllPasien()
    suspend fun getPasienByUser(userId: Int): PasienResponse = ApiConfig.getApiService().getPasienByUser(userId)
    suspend fun getPasienById(id: Int): PasienResponse = ApiConfig.getApiService().getPasienById(id)
    suspend fun insertPasien(
        userId: Long,
        nik: Long,
        nama: String,
        img: String? = null,
        kelamin: String,
        alamat: String,
        noTlp: String,
        tb: Int,
        bb: Int,
        status: String
    ): PasienResponse = ApiConfig.getApiService().insertPasien(
        userId, nik, nama, img, kelamin, alamat, noTlp, tb, bb, status
    )

    suspend fun updatePasien(
        id: Int,
        userId: Long,
        nik: Long,
        nama: String,
        img: String? = null,
        kelamin: String,
        alamat: String,
        noTlp: String,
        tb: Int,
        bb: Int,
        status: String
    ): PasienResponse = ApiConfig.getApiService().updatePasien(
        id, userId, nik, nama, img, kelamin, alamat, noTlp, tb, bb, status
    )

    // Dokter
    suspend fun getAllDokter(page: Int): DokterResponse = ApiConfig.getApiService().getAllDokter(page)
    suspend fun getDokterByUser(userId: Int): DokterResponse = ApiConfig.getApiService().getDokterByUser(userId)
    suspend fun getDokterById(id: Int): DokterResponse = ApiConfig.getApiService().getDokterById(id)
    suspend fun insertDokter(
        userId: Int,
        spesialisId: Int,
        titleDepan: String,
        nama: String,
        titleBelakang: String,
        img: String? = null,
        alamat: String,
        noTlp: String,
        tempatKerja: String,
        tahunLulus: Int,
        tglAktif: String,
        alumni: String,
        noReg: Long,
        jenisKelamin: String,
        status: String
    ): DokterResponse = ApiConfig.getApiService().insertDokter(
        userId, spesialisId, titleDepan, nama, titleBelakang, img, alamat, noTlp,
        tempatKerja, tahunLulus, tglAktif, alumni, noReg, jenisKelamin,status
    )
    suspend fun updateDokter(
        id: Long,
        userId: Long,
        spesialisId: Long,
        titleDepan: String,
        nama: String,
        titleBelakang: String,
        img: String? = null,
        alamat: String,
        noTlp: String,
        tempatKerja: String,
        tahunLulus: Int,
        tglAktif: String,
        alumni: String,
        noReg: Long,
        jenisKelamin: String,
        status: String
    ): DokterResponse = ApiConfig.getApiService().updateDokter(
        id, userId, spesialisId, titleDepan, nama, titleBelakang, img, alamat, noTlp,
        tempatKerja, tahunLulus, tglAktif, alumni, noReg, jenisKelamin,status
    )

    // Pasien Tambahan
    suspend fun getPasienTambahanByPasien(id: Int): PasienTambahanResponse =
        ApiConfig.getApiService().getPasienTambahanByPasien(id)
    suspend fun getPasienTambahanById(pasienId: Int, id: Int): PasienTambahanResponse =
        ApiConfig.getApiService().getPasienTambahanById(pasienId, id)
    suspend fun insertPasienTambahan(
        pasienId: Long,
        namaPasienTambahan: String,
        tb: Int,
        bb: Int,
        jenisKelamin: String,
        tglLahir: String,
        hubunganKeluarga: String
    ): PasienTambahanResponse = ApiConfig.getApiService().insertPasienTambahan(
        pasienId, namaPasienTambahan, tb, bb, jenisKelamin, tglLahir, hubunganKeluarga
    )

    suspend fun updatePasienTambahan(
        id: Int,
        pasienId: Long,
        namaPasienTambahan: String,
        tb: Int,
        bb: Int,
        jenisKelamin: String,
        tglLahir: String,
        hubunganKeluarga: String
    ): PasienTambahanResponse = ApiConfig.getApiService().updatePasienTambahan(
        id, pasienId, namaPasienTambahan, tb, bb, jenisKelamin, tglLahir, hubunganKeluarga
    )
    suspend fun deletePasien(id: Int): PasienTambahanResponse = ApiConfig.getApiService().deletePasienTambahan(id)

    // Upload Image
    suspend fun uploadImagePasien(
        id: Int, multipartBody: MultipartBody.Part
    ): PasienResponse = ApiConfig.getApiService().uploadImagePasien(id, multipartBody)

    suspend fun uploadImageDokter(
        id: Int, multipartBody: MultipartBody.Part
    ): PasienResponse = ApiConfig.getApiService().uploadImageDokter(id, multipartBody)

    // Janji
    suspend fun getJanjiByDokterId(id: Int): JanjiResponse =
        ApiConfig.getApiService().getJanjiByDokterId(id)
    suspend fun insertJanji(
        pasienId: Long,
        dokterId: Long,
        pasien_tambahanId: Long,
        sesiId: Long,
        jadwal: String,
        catatan: String,
        status: String
    ): JanjiResponse = ApiConfig.getApiService().insertJanji(
        pasienId, dokterId, pasien_tambahanId, sesiId, jadwal, catatan, status
    )

    suspend fun updateJanji(
        id: Int,
        pasienId: Long,
        dokterId: Long,
        pasien_tambahanId: Long,
        sesiId: Long,
        jadwal: String,
        catatan: String,
        status: String
    ): JanjiResponse = ApiConfig.getApiService().updatetJanji(
        id, pasienId, dokterId, pasien_tambahanId, sesiId, jadwal, catatan, status
    )

    // Konsultasi
    suspend fun getKonsultasiById(id: Int): KonsultasiResponse =
        ApiConfig.getApiService().getKonsultasiById(id)
    suspend fun getKonsultasiByPasienId(id: Int): KonsultasiResponse =
        ApiConfig.getApiService().getKonsultasiByPasienId(id)
    suspend fun getKonsultasiByDokterId(id: Int): KonsultasiResponse =
        ApiConfig.getApiService().getKonsultasiByDokterId(id)
    suspend fun insertKonsultasi(
        pasienId: Long,
        dokterId: Long,
        topik: String,
        status: String
    ): KonsultasiResponse = ApiConfig.getApiService().insertKonsultasi(pasienId, dokterId, topik, status)

    suspend fun updateKonsultasi(
        id: Int,
        pasienId: Long,
        dokterId: Long,
        topik: String,
        status: String
    ): KonsultasiResponse = ApiConfig.getApiService().updateKonsultasi(id, pasienId, dokterId, topik, status)

    // Diskusi
    suspend fun insertDisuksi(
        konsultasiId: Long,
        message: String
    ): DiskusiResponse = ApiConfig.getApiService().insertDiskusi(konsultasiId, message)

    // Catatan
    suspend fun getCatatanByKonsultasiId(id: Int): CatatanResponse =
        ApiConfig.getApiService().getCatatanByKonsultasiId(id)
    suspend fun insertCatatan(
        konsultasiId: Long,
        gejala: String,
        diagnosis: String,
        catatan: String
    ): CatatanResponse = ApiConfig.getApiService().insertCatatan(konsultasiId, gejala, diagnosis, catatan)

    suspend fun updateCatatan(
        id: Int,
        konsultasiId: Long,
        gejala: String,
        diagnosis: String,
        catatan: String
    ): CatatanResponse = ApiConfig.getApiService().updateCatatan(id, konsultasiId, gejala, diagnosis, catatan)

    // Sesi
    suspend fun getAllSesi(): SesiResponse = ApiConfig.getApiService().getAllSesi()

    // Artikel
    suspend fun getAllArtikel(): ArtikelResponse = ApiConfig.getApiService().getAllArtikel()

    // Room
    fun getUser(userId: Int): LiveData<List<User>> = mUserDao.getUser(userId)
    fun register(user: User): Long = mUserDao.insertUser(user)
    fun isEmailExist(email: String): LiveData<List<User>> = mUserDao.isEmailExist(email)

    //DataStore
    suspend fun setLoginStatus() = authDataStore.loginUser()
    suspend fun getLoginStatus(): Boolean = authDataStore.isLogin()
    suspend fun setLogoutStatus() = authDataStore.logoutUser()
    suspend fun setUserId(uid: Int) = authDataStore.setUserId(uid)
    suspend fun getUserId() = authDataStore.getUserId()
    suspend fun setUserRole(role: Int) = authDataStore.setUserRole(role)
    suspend fun getUserRole(): Int = authDataStore.getUserRole()

    companion object {
        @Volatile
        private var instances: UserRepository? = null

        fun getInstance(
            mUserDao: UserDao,
            executorService: ExecutorService,
            authDataStore: AuthDataStore
        ): UserRepository = instances ?: synchronized(this) {
            instances ?: UserRepository(mUserDao, executorService, authDataStore)
                .also { instances = it }
        }
    }
}