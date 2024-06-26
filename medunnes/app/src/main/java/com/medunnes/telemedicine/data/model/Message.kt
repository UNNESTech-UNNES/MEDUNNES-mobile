package com.medunnes.telemedicine.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    val email: String? = "",
    val text: String? = "",
    val timestamp: Long? = 0
)