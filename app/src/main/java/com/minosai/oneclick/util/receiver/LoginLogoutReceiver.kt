package com.minosai.oneclick.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.minosai.oneclick.util.helper.Constants
import com.minosai.oneclick.util.helper.LoginLogoutBroadcastHelper
import com.minosai.oneclick.util.receiver.listener.LoginLogoutListener
import com.minosai.oneclick.util.service.WebService
import dagger.android.AndroidInjection
import javax.inject.Inject

class LoginLogoutReceiver(val mLoginLogoutListener: LoginLogoutListener) : BroadcastReceiver() {

    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var webService: WebService

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    override fun onReceive(context: Context?, intent: Intent?) {

        AndroidInjection.inject(this, context)

        intent?.let { intent ->
            val type = intent.getStringExtra(LoginLogoutBroadcastHelper.EXTRA_TYPE)
            when(type) {
//                "LOGIN" -> mLoginLogoutListener.onLoggedListener(!webService.login())
//                "LOGOUT" -> mLoginLogoutListener.onLoggedListener(webService.logout())
            }
        }
    }

}