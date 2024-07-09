package com.medunnes.telemedicine.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "janji")
data class Janji(
    @PrimaryKey(autoGenerate = true)
    var janjidId: Int,

    @ColumnInfo(name = "datetime")
    var dateTime: String,

    @ColumnInfo(name = "sesi")
    var sesi : String,

    @ColumnInfo(name = "status")
    var status: String? = null,

    @ColumnInfo(name = "dokter_id")
    var dokterId: Int,

    @ColumnInfo(name = "pasien_id")
    var pasienId: Int
)
