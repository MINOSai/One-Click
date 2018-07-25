package com.minosai.oneclick.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.minosai.oneclick.util.helper.Constants
import com.minosai.oneclick.util.helper.LoginLogoutBroadcastHelper
import com.minosai.oneclick.util.service.WebService.Companion.RequestType
import com.minosai.oneclick.util.receiver.listener.LoginLogoutListener
import com.minosai.oneclick.util.service.WebService
import dagger.android.AndroidInjection
import org.jetbrains.anko.connectivityManager
import javax.inject.Inject

class LoginLogoutReceiver : BroadcastReceiver(), LoginLogoutListener {

    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var webService: WebService

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    override fun onReceive(context: Context?, intent: Intent?) {

        AndroidInjection.inject(this, context)

        context?.let {
            isWifiConnected(it)
        }
    }

    override fun onLoggedListener(requestType: WebService.Companion.RequestType, isLogged: Boolean) {

    }

    private fun isWifiConnected(context: Context) {
//        val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
//        if (info.isConnected) {
//            isConnectedToPronto()
//        }
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info: NetworkInfo = connectivityManager.activeNetworkInfo
        Toast.makeText(context, "isConnected : ${info.isConnected} to network: ${info.extraInfo}", Toast.LENGTH_SHORT).show()
        val wifiManager = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        if (wifiManager.isWifiEnabled) {
            isConnectedToPronto()
        }
    }

    private fun isConnectedToPronto() {
        Constants.URL_LOGIN.httpGet().timeout(500).responseString { _, _, result ->
            when (result) {
                is Result.Failure -> {

                }
                is Result.Success -> {
                    checkInternet()
                }
            }
        }
    }

    private fun checkInternet() {
        "https://www.example.com".httpGet().timeout(500).response { _, _, result ->
            when(result) {
                is Result.Failure -> {
                    webService.login(this)
                }
                is Result.Success -> {
                    webService.logout(this)
                }
            }
        }
    }

}