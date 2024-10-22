package com.medunnes.telemedicine.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.medunnes.telemedicine.data.model.Dokter
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.model.JanjiDanPasien
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.model.UserAndDokter

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE user_id = :user_id")
    fun getUser(user_id: Int): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Long

    @Update
    fun updateUser(user: User)

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun loginUser(email: String, password: String): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE email = :email")
    fun isEmailExist(email: String): LiveData<List<User>>

    @Transaction
    @Query("SELECT * FROM user WHERE user_id = :user_id")
    fun getUserAndDokter(user_id: Int): LiveData<List<UserAndDokter>>

    @Transaction
    @Query("SELECT * FROM user JOIN dokter ON user.user_id = dokter.user_id")
    fun getAllDokter(): LiveData<List<UserAndDokter>>

    @Transaction
    @Query("SELECT * FROM user JOIN dokter ON user.user_id = dokter.user_id WHERE spesialis_id = :speciality")
    fun getDokterBySpeciality(speciality: Int): LiveData<List<UserAndDokter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDokter(dokter: Dokter): Long

    @Update
    fun updateUserAndDokter(dokter: Dokter)

    @Query("SELECT * FROM janji")
    fun getAllJanji(): LiveData<List<JanjiDanPasien>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJanjiPasien(janji: Janji)

    @Update
    fun updateJanjiPasien(janji: Janji)

    @Transaction
    @Query("SELECT * FROM dokter " +
            "JOIN user ON dokter.user_id = user.user_id " +
            "WHERE dokter.dokterId = :dokterId")
    fun getDokterByDokterId(dokterId: Int): LiveData<List<UserAndDokter>>

    @Transaction
    @Query("SELECT * FROM janji " +
            "JOIN user ON janji.pasien_id = user.user_id " +
            "WHERE janji.dokter_id = :dokter_id")
    fun getJanjiAndPasien(dokter_id: Int): LiveData<List<JanjiDanPasien>>

    @Transaction
    @Query("SELECT * FROM janji " +
            "JOIN user ON janji.pasien_id = user.user_id " +
            "WHERE janji.pasien_id = :user_id")
    fun getDokterByJanji(user_id: Int): LiveData<List<JanjiDanPasien>>

    @Transaction
    @Query("SELECT * FROM pasien WHERE user_id = :uid")
    fun getPasienByUser(uid: Int): LiveData<List<Pasien>>

    @Query("SELECT * FROM pasien WHERE pasien_id = :pasienId")
    fun getPasienById(pasienId: Int): LiveData<List<Pasien>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPasien(pasien: Pasien)

    @Update
    fun updatePasien(pasien: Pasien)

    @Delete
    fun deletePasien(pasien: Pasien)
}