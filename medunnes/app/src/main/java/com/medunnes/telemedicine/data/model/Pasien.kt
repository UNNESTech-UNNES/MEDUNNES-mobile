package com.medunnes.telemedicine.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pasien")
data class Pasien(
    @PrimaryKey(autoGenerate = true)
    val pasienId: Int,

    @ColumnInfo(name = "nama_pasien")
    val namaPasien: String,

    @ColumnInfo(name = "hubungan")
    val hubungan: String? = null,

    @ColumnInfo(name = "tanggal_Lahir")
    val tanggalLahir: String? = null,

    @ColumnInfo(name = "user_id")
    val userId: Int
)
