package com.minosai.oneclick.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.getSSID
import com.minosai.oneclick.util.listener.WifiConnectivityListener
import com.minosai.oneclick.util.service.WebService
import dagger.android.AndroidInjection
import javax.inject.Inject

class WifiReceiver() : BroadcastReceiver() {

    constructor(wifiConnectivityListener: WifiConnectivityListener): this() {
        this.wifiConnectivityListener = wifiConnectivityListener
    }

    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var webService: WebService

    private val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    companion object {
        val SSID_LIST = listOf("\"VIT2.4G\"", "\"VIT5G\"", "\"VOLSBB\"", "\"VIT2.4\"", "\"VIT5\"", "\"VIT2-4G\"")
    }

    private var wifiConnectivityListener: WifiConnectivityListener? = null

    override fun onReceive(context: Context?, intent: Intent?) {

        AndroidInjection.inject(this, context)

        intent?.let { intent ->

            if (intent.action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION, true)) {

                val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                Log.i(TAG, "isConnected : ${info.isConnected} to network: ${info.extraInfo}")

                //TODO: auto login based on user preference - do it in main fragment
                if (info.isConnected) {
                    //TODO: What if SSID is not checked?
                    val ssid = info?.extraInfo ?: context?.getSSID()
                    if (ssid != null && ssid in SSID_LIST) {
                        wifiConnectivityListener?.onWifiStateChanged(true, ssid)
                    } else {
                        checkProntoNetworks(info.extraInfo)
                    }
                } else {
                    wifiConnectivityListener?.onWifiStateChanged(false, "")
                }
            }
        }
    }

    private fun checkProntoNetworks(ssid: String?) {
        Constants.URL_LOGIN.httpGet().response { request, response, result ->
            when(result) {
                is Result.Failure -> {
                    wifiConnectivityListener?.onWifiStateChanged(false, "")
                }
                is Result.Success -> {
                    wifiConnectivityListener?.onWifiStateChanged(true, ssid ?: "")
                }
            }
        }
    }
}