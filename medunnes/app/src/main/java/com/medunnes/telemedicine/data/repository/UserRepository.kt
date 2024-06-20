package com.medunnes.telemedicine.data.repository

import androidx.lifecycle.LiveData
import com.medunnes.telemedicine.data.api.ApiConfig
import com.medunnes.telemedicine.data.datastore.AuthDataStore
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.model.JanjiDanPasien
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.data.response.DokterResponse
import com.medunnes.telemedicine.data.response.JanjiResponse
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
    suspend fun getPasienByUser(userId: Int): PasienResponse = ApiConfig.getApiService().getPasienByUser(userId)
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

    // Janji
    suspend fun getJanjiByDokterId(id: Int): JanjiResponse = ApiConfig.getApiService().getJanjiByDokterId(id)
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

    // Sesi
    suspend fun getAllSesi(): SesiResponse = ApiConfig.getApiService().getAllSesi()

    // Room
    fun getUser(userId: Int): LiveData<List<User>> = mUserDao.getUser(userId)
    fun register(user: User): Long = mUserDao.insertUser(user)
    fun isEmailExist(email: String): LiveData<List<User>> = mUserDao.isEmailExist(email)
    fun updateProfile(user: User) = executorService.execute { mUserDao.updateUser(user) }
    fun getUserAndDokter(uid: Int): LiveData<List<UserAndDokter>> = mUserDao.getUserAndDokter(uid)
    fun getDokterBySpeciality(speciality: Int): LiveData<List<UserAndDokter>> = mUserDao.getDokterBySpeciality(speciality)
    fun getDokterByJanji(uid: Int): LiveData<List<JanjiDanPasien>> = mUserDao.getDokterByJanji(uid)
    fun getDokterByDokterId(dokterId: Int): LiveData<List<UserAndDokter>> = mUserDao.getDokterByDokterId(dokterId)
    fun getPasienById(pasienId: Int): LiveData<List<Pasien>> = mUserDao.getPasienById(pasienId)

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