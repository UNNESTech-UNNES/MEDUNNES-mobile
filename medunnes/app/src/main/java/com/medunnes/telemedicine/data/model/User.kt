package com.medunnes.telemedicine.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var id: Int,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "fullname")
    var fullname: String,

    @ColumnInfo(name = "titel_depan")
    var titelDepan: String? = null,

    @ColumnInfo(name = "titel_belakang")
    var titelBelakang: String? = null,

    @ColumnInfo(name = "no_str")
    var noStr: String? = null,

    @ColumnInfo(name = "tanggal_lahir")
    var tanggalLahir: String? = null,

    @ColumnInfo(name = "jenis_kelamin")
    var jenisKelamin: String? = null,

    @ColumnInfo(name = "alamat")
    var alamat: String? = null,

    @ColumnInfo(name = "tempat_praktik")
    var tempatPraktik: String? = null,

    @ColumnInfo(name = "no_telepon")
    var noTelepon: String? = null
)
