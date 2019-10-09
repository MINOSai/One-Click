package com.minosai.oneclick.util.helper

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.minosai.oneclick.network.WebService.Companion.RequestType
import com.minosai.oneclick.util.Constants.EXTRA_TYPE
import com.minosai.oneclick.util.Constants.LOGIN_LOGOUT_ACTION
import com.minosai.oneclick.util.receiver.LoginLogoutReceiver

object LoginLogoutBroadcastHelper {

    fun sendLoginLogoutBroadcast(context: Context, type: RequestType) {

        val intent = Intent(LOGIN_LOGOUT_ACTION)
        intent.putExtra(EXTRA_TYPE, type)
        context.sendBroadcast(intent)
    }

    fun getPendingIntent(context: Context): PendingIntent {
        val intent = getIntent(context)
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getIntent(context: Context) = Intent(context, LoginLogoutReceiver::class.java)

}