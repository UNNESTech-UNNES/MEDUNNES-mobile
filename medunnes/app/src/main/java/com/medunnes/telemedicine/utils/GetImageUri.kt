package com.medunnes.telemedicine.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.medunnes.telemedicine.BuildConfig
import com.medunnes.telemedicine.ui.profile.ProfileEditActivity
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private const val FILENAME_STORE = "yyyyMMdd_HHmmSS"
private val timeStamp: String = SimpleDateFormat(FILENAME_STORE, Locale.US).format(
    Date()
)

fun getImageUri(context: Context, sourceUri: Uri) : Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "IMG_$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/Medunnes")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

//        uri?.let {
//            try {
//                val inputStream: InputStream? = context.contentResolver.openInputStream(sourceUri)
//                val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
//
//                inputStream?.use { input ->
//                    outputStream?.use { output ->
//                        val buffer = ByteArray(1024)
//                        var bytesRead: Int
//                        while (input.read(buffer).also { bytesRead = it } != 0) {
//                            output.write(buffer, 0, bytesRead)
//                        }
//                    }
//                    Log.d("UP", inputStream.toString())
//                }
//
//            } catch (e: Exception) {
//                Log.d("ERROR", e.toString())
//                context.contentResolver.delete(uri, null, null)
//            }
//        }
    }

    return uri ?: getImageUriForPreQ(context)
}

private fun getImageUriForPreQ(context: Context) : Uri {
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/Medunnes/IMG_$timeStamp.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()

    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.provider",
        imageFile
    )
}