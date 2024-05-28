package com.medunnes.telemedicine.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.medunnes.telemedicine.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


private const val FILENAME_STORE = "yyyyMMdd_HHmmSS"
private val timeStamp: String = SimpleDateFormat(FILENAME_STORE, Locale.US).format(
    Date()
)
private lateinit var uuid: String

fun getImageUri(context: Context) : Uri {
    var uri: Uri? = null
    uuid = UUID.randomUUID().toString()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "IMG_${timeStamp}_$uuid.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/Medunnes")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }

    return uri ?: getImageUriForPreQ(context)
}

private fun getImageUriForPreQ(context: Context) : Uri {
    uuid = UUID.randomUUID().toString()
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/Medunnes/IMG_${timeStamp}_$uuid.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()

    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.provider",
        imageFile
    )
}