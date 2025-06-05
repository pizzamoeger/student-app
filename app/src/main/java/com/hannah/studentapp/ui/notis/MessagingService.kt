package com.hannah.studentapp.ui.notis

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.hannah.studentapp.R

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notification = NotificationCompat.Builder(this, "scheduled_notifications")
                .setContentTitle(it.title)
                .setContentText(it.body)
                .setSmallIcon(R.drawable.app_icon)  // your notification icon
                .build()

            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }
}
