package com.medunnes.telemedicine.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dokter")
data class Dokter(
    @PrimaryKey(autoGenerate = true)
    var dokterId: Int,

    @ColumnInfo(name = "titel_depan")
    var titelDepan: String? = null,

    @ColumnInfo(name = "titel_belakang")
    var titelBelakang: String? = null,

    @ColumnInfo(name = "no_str")
    var noStr: String? = null,

    @ColumnInfo(name = "tempat_praktik")
    var tempatPraktik: String? = null,

    @ColumnInfo(name = "user_id", )
    var userId: Int
)
