package com.minosai.oneclick.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import androidx.core.app.NotificationCompat
import com.minosai.oneclick.network.WebService
import com.minosai.oneclick.network.WebService.Companion.RequestType
import com.minosai.oneclick.util.Constants.EXTRA_TYPE
import com.minosai.oneclick.util.RepoInterface
import com.minosai.oneclick.util.listener.LoginLogoutListener
import com.minosai.oneclick.util.notification.NotificationUtil
import dagger.android.AndroidInjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject



class LoginLogoutReceiver : BroadcastReceiver(), LoginLogoutListener {

    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var webService: WebService
    @Inject
    lateinit var repoInterface: RepoInterface

    lateinit var builder: NotificationCompat.Builder
    lateinit var context: Context

    val TAG = javaClass.simpleName

    val listener: LoginLogoutListener = object : LoginLogoutListener {
        override fun onLoggedListener(requestType: RequestType, isLogged: Boolean, responseString: String) {
            builder.setContentText(responseString)
                    .setProgress(0, 0, false)
            NotificationUtil.notify(context, builder.build(), NotificationUtil.LOGIN_NOTIF_TAG)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        AndroidInjection.inject(this, context)

        var requestType: RequestType? = null

        try {
            requestType = intent?.getSerializableExtra(EXTRA_TYPE) as RequestType
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // TODO: start widget loading
        context?.let {
            this.context = it

            if (isWifiConnected(it)) {

                var title = "WiFi login"
                var message = "Logging in..."


                if (requestType != null && requestType == RequestType.LOGOUT) {
                    logout()
                    title = "WiFI logout"
                    message = "Logging out..."
                } else {
                    login()
                }

                builder =  NotificationUtil.getBaseBuilder(it, title, message)
                builder.setProgress(0, 0, true)
                NotificationUtil.notify(it, builder.build(), NotificationUtil.LOGIN_NOTIF_TAG)
            }
        }
    }

    private fun isWifiConnected(context: Context): Boolean {
        val wifiMgr = context.applicationContext
                ?.getSystemService(Context.WIFI_SERVICE) as WifiManager?

        return if (wifiMgr?.isWifiEnabled == true) {
            val wifiInfo = wifiMgr.connectionInfo
            wifiInfo.networkId != -1
        } else {
            false
        }
    }

    private fun login() {
        GlobalScope.launch {
            val account = repoInterface.getActiveAccount()
            withContext(Dispatchers.Main) {
                webService.login(listener, account.username, account.password)
            }
        }
    }

    private fun logout() {
        webService.logout(listener)
    }

    override fun onLoggedListener(requestType: RequestType, isLogged: Boolean, responseString: String) {
        showNotification(responseString)
    }

    private fun showNotification(responseString: String) {
        builder.setContentText(responseString)
                .setProgress(0, 0, false)
        NotificationUtil.notify(context, builder.build(), NotificationUtil.LOGIN_NOTIF_TAG)
    }
}