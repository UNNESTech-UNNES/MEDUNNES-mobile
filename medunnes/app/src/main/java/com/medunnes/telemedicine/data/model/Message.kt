package com.medunnes.telemedicine.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    val UUID: String,
    val text: String? = null,
    val timestamp: Long? = null
)