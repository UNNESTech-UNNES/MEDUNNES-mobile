package com.medunnes.telemedicine.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.room.dao.UserDao

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context) : UserDatabase {
            if (INSTANCE == null) {
                synchronized(UserDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java, "user"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as UserDatabase
        }
    }

}