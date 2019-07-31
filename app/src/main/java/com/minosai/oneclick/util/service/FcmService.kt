package com.minosai.oneclick.util.service

import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.minosai.oneclick.util.notification.NotificationUtil

class FcmService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        remoteMessage?.let {
            val body = it.notification?.body ?: ""
            val title = it.notification?.title ?: "One Click"

            val builder = NotificationUtil.getBaseBuilder(this, title, body).apply {
                setStyle(NotificationCompat.BigTextStyle()
                        .bigText(body)
                        .setBigContentTitle(title)
                        .setSummaryText("Message from One Click"))
            }

            NotificationUtil.notify(this, builder.build(), NotificationUtil.FCM_NOTIF_TAG)
        }
    }
}