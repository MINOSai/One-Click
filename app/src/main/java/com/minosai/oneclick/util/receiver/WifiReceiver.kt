package com.minosai.oneclick.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import android.net.NetworkInfo
import android.util.Log
import android.net.wifi.WifiInfo
import android.support.v4.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.support.v4.app.Fragment
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.service.WebService
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasBroadcastReceiverInjector
import javax.inject.Inject


class WifiReceiver : BroadcastReceiver() {

    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var webService: WebService

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    override fun onReceive(context: Context?, intent: Intent?) {

        AndroidInjection.inject(this, context)

        intent?.let { intent ->
            if (intent.action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION, true)) {
                val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                var isConnected = false
                if (info.isConnected) {
                    isConnected = true
                    webService.isOnline()
                }
                Log.i(TAG, "isConnected : $isConnected")
                preferences.edit()
                        .putBoolean(Constants.PREF_ISWIFICONNECTED, isConnected)
                        .apply()
                //TODO: auto login based on user preference
            }
        }
    }

}