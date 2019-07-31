package com.minosai.oneclick.util.service

import android.annotation.TargetApi
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Handler
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.minosai.oneclick.R
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.network.WebService
import com.minosai.oneclick.network.WebService.Companion.RequestType
import com.minosai.oneclick.util.RepoInterface
import com.minosai.oneclick.util.listener.LoginLogoutListener
import com.minosai.oneclick.util.listener.WifiConnectivityListener
import com.minosai.oneclick.util.receiver.WifiReceiver
import dagger.android.AndroidInjection
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
@TargetApi(Build.VERSION_CODES.N)
class OneClickTileService : TileService(),
        WifiConnectivityListener,
        LoginLogoutListener {

    val TAG = javaClass.simpleName

    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var webService: WebService
    @Inject
    lateinit var repoInterface: RepoInterface

    private lateinit var wifiReceiver: WifiReceiver

    private var activeAccount: AccountInfo? = null

    private val observer = Observer<AccountInfo> {
        activeAccount = it
        if (repoInterface.userPrefs.loginQsTile) {
            login()
        }
    }

    override fun onCreate() {
        super.onCreate()

        AndroidInjection.inject(this)

        wifiReceiver = WifiReceiver(this)

        registerWifiReceiver()

        repoInterface.updateAccounts()

        repoInterface.activeAccount.observeForever(observer)
    }

    override fun onClick() {
        // TODO: if no account found open app
        if (qsTile.state != Tile.STATE_UNAVAILABLE && activeAccount != null) {
            login()
        }
    }

    private fun login() {
        unlockAndRun {
            webService.login(
                    this,
                    activeAccount!!.username,
                    activeAccount!!.password
            )
            changeStateLoading()
        }
    }

    override fun onDestroy() {
        unregisterWifiReceiver()
        super.onDestroy()
        repoInterface.activeAccount.removeObserver(observer)
    }


    private fun registerWifiReceiver() {
        try {
            val intentFilter = with(IntentFilter()) {
                addAction("android.net.wifi.WIFI_SATE_CHANGED")
                addAction("android.net.conn.CONNECTIVITY_CHANGE")
                addAction("android.net.wifi.supplicant.CONNECTION_CHANGE")
                addAction("android.net.wifi.STATE_CHANGE")
                return@with this
            }
            registerReceiver(wifiReceiver, intentFilter)
            Log.d(TAG, "WifiReceiver registered")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "WifiReceiver already registered")
        }
    }

    private fun unregisterWifiReceiver() {
        try {
            unregisterReceiver(wifiReceiver)
        } catch (e: Exception) {
            // already unregistered
        }
    }

    override fun onWifiStateChanged(isConnectedToWifi: Boolean, ssid: String) {
        updateState(isConnectedToWifi)
    }

    override fun onLoggedListener(requestType: RequestType, isLogged: Boolean, responseString: String) {

        if (requestType == RequestType.LOGIN) {
            if (isLogged) {
                changeStateSuccess()
            } else {
                changeStateFailure()
            }
        }

        Handler().postDelayed({
            updateState(true)
        }, 1500)
    }

    private fun updateState(isWifiConnected: Boolean) {
        with(qsTile) {

            icon = Icon.createWithResource(this@OneClickTileService, R.drawable.ic_login)

            if (isWifiConnected) {
                state = Tile.STATE_INACTIVE
                label = "Login"
            } else {
                state = Tile.STATE_UNAVAILABLE
                label = "One Click"
            }

            updateTile()
        }
    }

    private fun changeStateLoading() = with(qsTile) {
        state = Tile.STATE_UNAVAILABLE
        label = "Loading"
        icon = Icon.createWithResource(this@OneClickTileService, R.drawable.ic_dots_horizontal)
        updateTile()
    }

    private fun changeStateSuccess() = with(qsTile) {
        state = Tile.STATE_ACTIVE
        label = "Success"
        icon = Icon.createWithResource(this@OneClickTileService, R.drawable.ic_done_white_48dp)
        updateTile()
    }

    private fun changeStateFailure() = with(qsTile) {
        state = Tile.STATE_ACTIVE
        label = "Failed"
        icon = Icon.createWithResource(this@OneClickTileService, R.drawable.ic_error_outline_black_24dp)
        updateTile()
    }
}
