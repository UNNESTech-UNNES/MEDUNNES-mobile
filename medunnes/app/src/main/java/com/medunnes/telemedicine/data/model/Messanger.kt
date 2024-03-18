package com.medunnes.telemedicine.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Messanger(
    val fotoMessanger: String,
    val namaMessanger: String,
    val sesiMessanger: String,
    val startusMessanger: String
) : Parcelable