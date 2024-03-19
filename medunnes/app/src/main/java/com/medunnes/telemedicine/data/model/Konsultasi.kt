package com.medunnes.telemedicine.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Konsultasi(
    val fotoPatient: String,
    val namaPatient: String,
    val sesiPatient: String,
    val statusPatient: String
) : Parcelable
