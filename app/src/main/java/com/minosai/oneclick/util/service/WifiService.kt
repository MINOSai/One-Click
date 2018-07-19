package com.minosai.oneclick.util.service

import android.app.Service
import android.content.Intent

class WifiService : Service() {

//    val TAG = javaClass.simpleName
//    private var wifiReceiver = WifiReceiver()
//    var isReceivingWifiStatus = false
//    @Inject
//    lateinit var preferences: SharedPreferences
//
    override fun onBind(p0: Intent?) = null
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        onTaskRemoved(intent)
//        return START_STICKY
//    }
//
//    override fun onCreate() {
//        AndroidInjection.inject(this)
//        Log.d(TAG, "wifiservice - oncreate")
//        super.onCreate()
////        isReceivingWifiStatus = preferences.getBoolean(Constants.PREF_ISRECEIVINGWIFI, false)
////        if (!isReceivingWifiStatus) {
////            val intentFilter = IntentFilter()
////            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
////            applicationContext.registerReceiver(wifiReceiver, intentFilter)
////            preferences.edit()
////                    .putBoolean(Constants.PREF_ISRECEIVINGWIFI, true)
////                    .apply()
////        }
//        try {
//            val intentFilter = IntentFilter()
//            with(intentFilter) {
//                addAction("android.net.wifi.WIFI_SATE_CHANGED")
//                addAction("android.net.conn.CONNECTIVITY_CHANGE")
//                addAction("android.net.wifi.supplicant.CONNECTION_CHANGE")
//                addAction("android.net.wifi.STATE_CHANGE")
//            }
//            registerReceiver(WifiReceiver(), intentFilter)
//            Log.d(TAG, "WifiReceiver registered")
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.d(TAG, "WifiReceiver already registered")
//        }
//    }
//
//    override fun onDestroy() {
////        isReceivingWifiStatus = preferences.getBoolean(Constants.PREF_ISRECEIVINGWIFI, false)
////        if (isReceivingWifiStatus) {
////            applicationContext.unregisterReceiver(wifiReceiver)
////            preferences.edit()
////                    .putBoolean(Constants.PREF_ISRECEIVINGWIFI, false)
////                    .apply()
////        }
//        try {
//            applicationContext.unregisterReceiver(wifiReceiver)
//            Log.d(TAG, "WifiReceiver UNregistered")
//        } catch (e: Exception) {
//            // already unregistered
//            Log.d(TAG, "WifiReceiver already UNregistered")
//        }
//        super.onDestroy()
//    }
//
//    override fun onTaskRemoved(rootIntent: Intent?) {
//        val restartServiceIntent = Intent(applicationContext, javaClass)
//        restartServiceIntent.setPackage(packageName)
//        startService(restartServiceIntent)
//        super.onTaskRemoved(rootIntent)
//    }

}