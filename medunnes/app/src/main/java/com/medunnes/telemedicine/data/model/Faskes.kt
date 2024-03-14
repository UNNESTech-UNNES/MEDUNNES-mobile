package com.medunnes.telemedicine.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Faskes(
    val namaFaskes: String,
    val fotoFaskes: String
) : Parcelable
