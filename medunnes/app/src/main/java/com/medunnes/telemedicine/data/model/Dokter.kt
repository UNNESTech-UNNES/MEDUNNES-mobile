package com.medunnes.telemedicine.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dokter")
data class Dokter(
    @PrimaryKey(autoGenerate = true)
    var dokterId: Int,

    @ColumnInfo(name = "user_id")
    var userId: Int,

    @ColumnInfo(name = "spesialis_id")
    var spesialidId: Int,

    @ColumnInfo(name = "titel_depan")
    var titelDepan: String? = null,

    @ColumnInfo(name = "nama_dokter")
    var namaDokter: String? = null,

    @ColumnInfo(name = "titel_belakang")
    var titelBelakang: String? = null,

    @ColumnInfo(name = "img_dokter")
    var imgDokter: String? = null,

    @ColumnInfo(name = "alamat")
    var alamat: String? = null,

    @ColumnInfo(name = "no_tlp")
    var noTlp: String? = null,

    @ColumnInfo(name = "tempat_kerja")
    var tempatKerja: String? = null,

    @ColumnInfo(name = "tempat_lulus")
    var tempatLulus: String? = null,

    @ColumnInfo(name = "tgl_mulai_aktif")
    var tglMulaiAktif: String? = null,

    @ColumnInfo(name = "alumni")
    var alumni: String? = null,

    @ColumnInfo(name = "no_reg")
    var noReg: Long? = null,

    @ColumnInfo(name = "jenis_kelamin")
    var jenisKelamin: Long? = null,

    @ColumnInfo(name = "status")
    var status: Long? = null,
)
