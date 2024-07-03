package com.medunnes.telemedicine.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ui.main.MainActivity


// Kirim notifikasi -> belum terpakai
class MedunnesFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d(TAG, "token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "From: ${message.from}")
        Log.d(TAG, "Message data payload: " + message.data)
        Log.d(TAG, "Message Notification Body: ${message.notification?.body}")

        sendNotification(message.notification?.title, message.notification?.body)
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val contentIntent = Intent(applicationContext, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notifiactionBuilder = notificationBuilder(title, messageBody, contentPendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notifiactionBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notifiactionBuilder.build())
    }

    private fun notificationBuilder(
        title: String?,
        messageBody: String?,
        contentPendingIntent: PendingIntent
    ): NotificationCompat.Builder {

        return NotificationCompat.Builder(
            applicationContext, NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.message)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
    }

    companion object {
        const val TAG = "MedunnesFirebaseMessagingService"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL_ID = "firebase channel"
        const val NOTIFICATION_CHANNEL_NAME = "Firebase Notification"
    }

}