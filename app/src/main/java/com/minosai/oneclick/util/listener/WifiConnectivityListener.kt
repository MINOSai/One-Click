package com.minosai.oneclick.util.listener

interface WifiConnectivityListener {

    fun onWifiStateChanged(isConnectedToWifi: Boolean, ssid: String)

}