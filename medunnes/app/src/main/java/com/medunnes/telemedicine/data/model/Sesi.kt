package com.medunnes.telemedicine.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sesi(
    val noSesi: Int,
    val waktuSesi: String
) : Parcelable
