package com.medunnes.telemedicine.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artikel(
    val judulArtikel: String,
    val headlineArtikel: String
) : Parcelable
