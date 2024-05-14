package com.medunnes.telemedicine.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserAndDokter(
    @Embedded
    val user: User,

    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id"
    )
    val dokter: Dokter
)
