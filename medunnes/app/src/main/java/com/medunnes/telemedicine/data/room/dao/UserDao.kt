package com.medunnes.telemedicine.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medunnes.telemedicine.data.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE user_id = :user_id")
    fun getUser(user_id: Int): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)
}