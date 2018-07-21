package com.minosai.oneclick.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import android.net.NetworkInfo
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.minosai.oneclick.util.helper.Constants
import com.minosai.oneclick.util.receiver.listener.WifiConnectivityListener
import com.minosai.oneclick.util.service.WebService
import dagger.android.AndroidInjection
import javax.inject.Inject


class WifiReceiver(private val wifiConnectivityListener: WifiConnectivityListener) : BroadcastReceiver() {

    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var webService: WebService

    private val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    override fun onReceive(context: Context?, intent: Intent?) {

//        Log.d(TAG, "wifireceiver - onreceive()")

        AndroidInjection.inject(this, context)

        intent?.let { intent ->
            if (intent.action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION, true)) {
                val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                val isConnected = info.isConnected
                Log.i(TAG, "isConnected : $isConnected")
//                wifiConnectivityListener.onWifiStateChanged(isConnected)
                isConnectedToPronto()
                //TODO: auto login based on user preference
            }
        }
    }

    private fun isConnectedToPronto() {
        Constants.URL_LOGIN.httpGet().timeout(500).responseString { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    wifiConnectivityListener.onWifiStateChanged(false)
                }
                is Result.Success -> {
                    wifiConnectivityListener.onWifiStateChanged(true)
                }
            }
        }
    }

}