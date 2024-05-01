package com.medunnes.telemedicine.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class JanjiDanPasien(
    @Embedded
    val janji: Janji,
    @Relation(
        parentColumn = "pasien_id",
        entityColumn = "user_id",
    )
    val user: User
)