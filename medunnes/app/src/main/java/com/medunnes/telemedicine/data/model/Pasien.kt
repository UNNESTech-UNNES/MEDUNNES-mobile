package com.medunnes.telemedicine.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pasien")
data class Pasien(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pasien_id")
    val pasienId: Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "nik")
    val nik: Long,

    @ColumnInfo(name = "nama_pasien")
    val namaPasien: String,

    @ColumnInfo(name = "img_pasien")
    val imgPasien: String? = null,

    @ColumnInfo(name = "jenis_kelamin")
    val jenisKelamin: String,

    @ColumnInfo(name = "alamat")
    val alamat: String,

    @ColumnInfo(name = "no_tlp")
    val noTlp: String,

    @ColumnInfo(name = "tb")
    val tb: Int,

    @ColumnInfo(name = "bb")
    val bb: Int,

    @ColumnInfo(name = "status")
    val status: String
)
