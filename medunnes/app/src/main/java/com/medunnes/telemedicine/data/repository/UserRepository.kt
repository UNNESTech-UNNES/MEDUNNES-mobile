package com.medunnes.telemedicine.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.medunnes.telemedicine.data.api.ApiConfig
import com.medunnes.telemedicine.data.api.ApiService
import com.medunnes.telemedicine.data.datastore.AuthDataStore
import com.medunnes.telemedicine.data.model.Dokter
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.model.JanjiDanPasien
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.data.response.UserResponse
import com.medunnes.telemedicine.data.room.dao.UserDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository private constructor(
    private val mUserDao: UserDao,
    private val application: Application,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor(),
    private val authDataStore: AuthDataStore
){

    fun getUser(userId: Int): LiveData<List<User>> = mUserDao.getUser(userId)
    fun register(user: User): Long = mUserDao.insertUser(user)
    fun login(email: String, password: String): LiveData<List<User>> = mUserDao.loginUser(email, password)
    fun isEmailExist(email: String): LiveData<List<User>> = mUserDao.isEmailExist(email)
    fun updateProfile(user: User) = executorService.execute { mUserDao.updateUser(user) }
    fun registerDokter(dokter: Dokter): Long = mUserDao.insertDokter(dokter)
    fun getAllDokter(): LiveData<List<UserAndDokter>> = mUserDao.getAllDokter()
    fun getUserAndDokter(uid: Int): LiveData<List<UserAndDokter>> = mUserDao.getUserAndDokter(uid)
    fun updateDokter(dokter: Dokter) = mUserDao.updateUserAndDokter(dokter)
    fun getDokterBySpeciality(speciality: String): LiveData<List<UserAndDokter>> = mUserDao.getDokterBySpeciality(speciality)
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

    suspend fun setLoginStatus() = authDataStore.loginUser()
    suspend fun getLoginStatus(): Boolean = authDataStore.isLogin()
    suspend fun setLogoutStatus() = authDataStore.logoutUser()
    suspend fun setUserId(uid: Int) = authDataStore.setUserId(uid)
    suspend fun getUserId() = authDataStore.getUserId()
    suspend fun setUserRole(role: Int) = authDataStore.setUserRole(role)
    suspend fun getUserRole(): Int = authDataStore.getUserRole()

//    suspend fun getAllUser(page: String): Call<UserResponse> {
//        val client = ApiConfig.getApiService().getAllUsers(page)
//        client.enqueue(object : Callback<UserResponse> {
//            override fun onResponse(
//                call: Call<UserResponse>,
//                response: Response<UserResponse>
//            ) {
//                try {
//                    if (response.isSuccessful) {
//                        val responseBody = response.body()
//                        Log.d("RESPONSE", responseBody.toString())
//                    } else {
//                        Log.d("RESPONSE", "FAILED")
//                    }
//                } catch (e: Exception) {
//                    Log.d("RESPONSE", e.toString())
//                }
//            }
//
//            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
//                Log.d("ERROR", t.toString())
//            }
//
//        })
//    }

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