package com.medunnes.telemedicine.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class JanjiDanPasien(
    @Embedded
    val user: User,

    @Relation(
        entity = Janji::class,
        parentColumn = "user_id",
        entityColumn = "pasien_id",
    )
    val janji: Janji
)