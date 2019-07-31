package com.minosai.oneclick.util.notification

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.minosai.oneclick.MainActivity
import com.minosai.oneclick.R


object NotificationUtil {

    const val NOTIF_CHANNEL_ID = "com.minosai.oneclick.NOTIF_CHANNEL_ID"
    const val LOGIN_NOTIF_TAG = "LOGIN_NOTIF_TAG"
    const val FCM_NOTIF_TAG = "FCM_NOTIF_TAG"

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    fun notify(context: Context, notification: Notification, tag: String) {
        val nm = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "One Click"
            val description = "Shows login progress and important announcements"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIF_CHANNEL_ID, name, importance)
            channel.description = description
            nm.createNotificationChannel(channel)
        }

        nm.notify(tag, 0, notification)
    }

    fun getBaseBuilder(context: Context, title: String, message: String) = NotificationCompat.Builder(context)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_login)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setChannelId(NOTIF_CHANNEL_ID)
            .setContentIntent(
                    PendingIntent.getActivity(
                            context, 0,
                            Intent(context, MainActivity::class.java),
                            PendingIntent.FLAG_UPDATE_CURRENT
                    )
            )
            .setAutoCancel(true)
}