package com.minosai.oneclick.util.receiver.listener

interface WifiConnectivityListener {

    fun onWifiStateChanged(isConnectedToWifi: Boolean, ssid: String)

}