package com.minosai.oneclick.util.helper

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.minosai.oneclick.util.receiver.LoginLogoutReceiver
import com.minosai.oneclick.util.service.WebService.Companion.RequestType

object LoginLogoutBroadcastHelper {

    val LOGIN_LOGOUT_ACTION = "${Constants.PACKAGE_NAME}.action.LOGIN_LOGOUT_ACTION"
    val EXTRA_TYPE = "${Constants.PACKAGE_NAME}.extra.REQUEST_TYPE"

    fun sendLoginLogoutBroadcast(context: Context, type: RequestType) {

        val intent = Intent(LOGIN_LOGOUT_ACTION)
        intent.putExtra(EXTRA_TYPE, type)
        context.sendBroadcast(intent)
    }

    fun getPendingIntent(context: Context): PendingIntent {
        val intent = getIntent(context)
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun getIntent(context: Context) = Intent(context, LoginLogoutReceiver::class.java)

}