package com.medunnes.telemedicine.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.medunnes.telemedicine.data.model.Dokter
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.model.JanjiDanPasien
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
    @Query("SELECT * FROM user JOIN dokter ON user.user_id = dokter.user_id WHERE spesialis = :speciality")
    fun getDokterBySpeciality(speciality: String): LiveData<List<UserAndDokter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDokter(dokter: Dokter)

    @Update
    fun updateUserAndDokter(dokter: Dokter)

    @Query("SELECT * FROM janji")
    fun getAllJanji(): LiveData<List<Janji>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJanjiPasien(janji: Janji)

    @Update
    fun updateJanjiPasien(janji: Janji)

    @Transaction
    @Query("SELECT * FROM janji " +
            "JOIN user ON user.user_id = janji.pasien_id WHERE dokter_id = :dokter_id")
    fun getJanjiAndPasien(dokter_id: Int): LiveData<List<JanjiDanPasien>>
}