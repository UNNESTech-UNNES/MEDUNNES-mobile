package com.medunnes.telemedicine.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.medunnes.telemedicine.data.api.ApiConfig
import com.medunnes.telemedicine.data.datastore.AuthDataStore
import com.medunnes.telemedicine.data.model.Dokter
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.model.JanjiDanPasien
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.data.model.UserAndPasien
import com.medunnes.telemedicine.data.response.DokterResponse
import com.medunnes.telemedicine.data.response.LoginResponse
import com.medunnes.telemedicine.data.response.PasienResponse
import com.medunnes.telemedicine.data.response.UserResponse
import com.medunnes.telemedicine.data.room.dao.UserDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository private constructor(
    private val mUserDao: UserDao,
    private val application: Application,
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
    suspend fun getAllPasien(page: String): PasienResponse = ApiConfig.getApiService().getAllPasien(page)
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

    // Dokter
    suspend fun getDokterByUser(userId: Int): DokterResponse = ApiConfig.getApiService().getDokterByUser(userId)
    suspend fun insertDokter(
        userId: Int,
        spesialisId: Int,
        titleDepan: String,
        nama: String,
        titleBelakang: String,
        img: String,
        alamat: String,
        noTlp: String,
        tempatKerja: String,
        tempatLulue: Int,
        tglAktif: String,
        alumni: String,
        noReg: Int
    ): DokterResponse = ApiConfig.getApiService().insertDokter(
        userId, spesialisId, titleDepan, nama, titleBelakang, img, alamat, noTlp, tempatKerja, tempatLulue, tglAktif, alumni, noReg
    )

    // Room
    fun getUser(userId: Int): LiveData<List<User>> = mUserDao.getUser(userId)
    fun register(user: User): Long = mUserDao.insertUser(user)
    fun isEmailExist(email: String): LiveData<List<User>> = mUserDao.isEmailExist(email)
    fun updateProfile(user: User) = executorService.execute { mUserDao.updateUser(user) }
    fun registerDokter(dokter: Dokter): Long = mUserDao.insertDokter(dokter)
    fun getAllDokter(): LiveData<List<UserAndDokter>> = mUserDao.getAllDokter()
    fun getUserAndDokter(uid: Int): LiveData<List<UserAndDokter>> = mUserDao.getUserAndDokter(uid)
    fun updateDokter(dokter: Dokter) = mUserDao.updateUserAndDokter(dokter)
    fun getDokterBySpeciality(speciality: Int): LiveData<List<UserAndDokter>> = mUserDao.getDokterBySpeciality(speciality)
    fun getJanji(): LiveData<List<JanjiDanPasien>> = mUserDao.getAllJanji()
    fun getJanjiAndPasien(dokter_id: Int): LiveData<List<JanjiDanPasien>> = mUserDao.getJanjiAndPasien(dokter_id)
    fun insertJanjiPasien(janji: Janji) = executorService.execute { mUserDao.insertJanjiPasien(janji) }
    fun updateJanjiPasien(janji: Janji) = executorService.execute { mUserDao.updateJanjiPasien(janji) }
    fun getDokterByJanji(uid: Int): LiveData<List<JanjiDanPasien>> = mUserDao.getDokterByJanji(uid)
    fun getDokterByDokterId(dokterId: Int): LiveData<List<UserAndDokter>> = mUserDao.getDokterByDokterId(dokterId)
    fun getPasiebByUser(uid: Int): LiveData<List<Pasien>> = mUserDao.getPasienByUser(uid)
    fun getPasienById(pasienId: Int): LiveData<List<Pasien>> = mUserDao.getPasienById(pasienId)
    fun insertPasien(pasien: Pasien) = executorService.execute { mUserDao.insertPasien(pasien) }
    fun updatePasien(pasien: Pasien) = executorService.execute { mUserDao.updatePasien(pasien) }
    fun deletePasien(pasien: Pasien) = executorService.execute { mUserDao.deletePasien(pasien) }

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
            application: Application,
            executorService: ExecutorService,
            authDataStore: AuthDataStore
        ): UserRepository = instances ?: synchronized(this) {
            instances ?: UserRepository(mUserDao, application, executorService, authDataStore)
                .also { instances = it }
        }
    }
}