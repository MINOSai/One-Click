package com.minosai.oneclick.util.helper

import android.content.Context
import android.content.Intent

object LoginLogoutBroadcastHelper {

    val LOGIN_LOGOUT_ACTION = "${Constants.PACKAGE_NAME}.action.LOGIN_LOGOUT_ACTION"
    val EXTRA_TYPE = "${Constants.PACKAGE_NAME}.extra.TYPE"

    fun sendLoginLogoutBroadcast(context: Context, type: String) {

        val intent = Intent(LOGIN_LOGOUT_ACTION)
        intent.putExtra(EXTRA_TYPE, type)
        context.sendBroadcast(intent)
    }

}