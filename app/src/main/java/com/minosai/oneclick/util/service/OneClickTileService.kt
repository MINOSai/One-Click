package com.minosai.oneclick.util.service

import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.drawable.Icon
import android.net.wifi.WifiManager
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.support.annotation.RequiresApi
import android.util.Log
import com.minosai.oneclick.R
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.receiver.WifiReceiver
import dagger.android.AndroidInjection
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
class OneClickTileService : TileService() {

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var webService: WebService

    private var wifiReceiver = WifiReceiver()
    var isReceivingWifiStatus = false

    private var isLogged = false
    private var isWifiConnected = false
    private var isOnline = false

    private var userName = ""
    private var password = ""

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
        when(key) {
            Constants.PREF_ISWIFICONNECTED -> isWifiConnected = preferences.getBoolean(Constants.PREF_ISWIFICONNECTED, false)
            Constants.PREF_ISONLINE -> isOnline = preferences.getBoolean(Constants.PREF_ISONLINE, false)
            Constants.PREF_ISLOGGED -> isLogged = preferences.getBoolean(Constants.PREF_ISLOGGED, false)
        }
        updateState()
    }

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)

        try {
            val intentFilter = IntentFilter()
            with(intentFilter) {
                addAction("android.net.wifi.WIFI_SATE_CHANGED")
                addAction("android.net.conn.CONNECTIVITY_CHANGE")
                addAction("android.net.wifi.supplicant.CONNECTION_CHANGE")
                addAction("android.net.wifi.STATE_CHANGE")
            }
            registerReceiver(WifiReceiver(), intentFilter)
            Log.d(TAG, "WifiReceiver registered")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "WifiReceiver already registered")
        }
    }

    override fun onDestroy() {
        try {
            applicationContext.unregisterReceiver(wifiReceiver)
            Log.d(TAG, "WifiReceiver UNregistered")
        } catch (e: Exception) {
            // already unregistered
            Log.d(TAG, "WifiReceiver already UNregistered")
        }
        super.onDestroy()
    }

    override fun onClick() {
        super.onClick()
        when(qsTile.state) {
            Tile.STATE_ACTIVE -> webService.logout()
            Tile.STATE_INACTIVE -> webService.login(userName, password)
        }
        updateState()
    }

    override fun onStartListening() {
        super.onStartListening()
        preferences.registerOnSharedPreferenceChangeListener(listener)
        getStoredValues()
    }

    override fun onStopListening() {
        super.onStopListening()
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    private fun getStoredValues() {
        with(preferences) {
            isLogged = getBoolean(Constants.PREF_ISLOGGED, false)
            isWifiConnected = getBoolean(Constants.PREF_ISWIFICONNECTED, false)
            isOnline = getBoolean(Constants.PREF_ISONLINE, false)
            userName = getString(Constants.PREF_USERNAME, "")
            password = getString(Constants.PREF_PASSWORD, "")
        }
        updateState()
    }

    private fun updateState() {
        //TODO: Check if online here because loggin into other device logs out here and we will not know
        if (isWifiConnected) {
            if (isOnline) changeStateLogout() else changeStateLogin()
        } else {
            changeStateDisable()
        }
    }

    private fun changeStateDisable() = with(qsTile) {
        state = Tile.STATE_UNAVAILABLE
        label = "One click"
        updateTile()
    }

    private fun changeStateLogin() = with(qsTile) {
        state = Tile.STATE_INACTIVE
        icon = Icon.createWithResource(this@OneClickTileService, R.drawable.ic_login)
        label = "Login"
        updateTile()
    }

    private fun changeStateLogout() = with(qsTile) {
        state = Tile.STATE_ACTIVE
        icon = Icon.createWithResource(this@OneClickTileService, R.drawable.ic_logout)
        label = "Logout"
        updateTile()
    }

    override fun onTileAdded() {
        super.onTileAdded()
//        if (!isReceivingWifiStatus) {
//            val intentFilter = IntentFilter()
//            intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
//            registerReceiver(wifiReceiver, intentFilter)
//            isReceivingWifiStatus = true
//        }
    }

    override fun onTileRemoved() {
//        if (isReceivingWifiStatus) {
//            unregisterReceiver(wifiReceiver)
//            isReceivingWifiStatus = false
//        }
        super.onTileRemoved()
    }
}
