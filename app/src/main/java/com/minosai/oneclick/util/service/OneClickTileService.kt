package com.minosai.oneclick.util.service

import android.annotation.TargetApi
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Handler
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.crashlytics.android.Crashlytics
import com.minosai.oneclick.R
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.network.WebService
import com.minosai.oneclick.network.WebService.Companion.RequestType
import com.minosai.oneclick.util.RepoInterface
import com.minosai.oneclick.util.listener.LoginLogoutListener
import dagger.android.AndroidInjection
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
@TargetApi(Build.VERSION_CODES.N)
class OneClickTileService : TileService(),
//        WifiConnectivityListener,
        LoginLogoutListener {

//    val TAG = javaClass.simpleName
    val TAG = "oneclicktileservice"

//    @Inject
//    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var webService: WebService
    @Inject
    lateinit var repoInterface: RepoInterface

//    private lateinit var wifiReceiver: WifiReceiver

    private var activeAccount: AccountInfo? = null

    private val observer = Observer<AccountInfo> {
        activeAccount = it
        if (repoInterface.userPrefs.loginQsTile) {
            login()
        }
    }

//    override fun onTileAdded() {
//        super.onTileAdded()
//        changeStateNormal()
//        Log.d(TAG, "onTileAdded")
//    }

    override fun onCreate() {
        super.onCreate()

        AndroidInjection.inject(this)

//        wifiReceiver = WifiReceiver(this)
//        changeStateNormal()

//        registerWifiReceiver()

//        repoInterface.updateAccounts()

//        repoInterface.activeAccount.observeForever(observer)

        Log.d(TAG, "onCreate")
    }

    override fun onStartListening() {
        super.onStartListening()
        repoInterface.activeAccount.observeForever(observer)
        changeStateNormal()
        Log.d(TAG, "onStartListening")
    }

    override fun onClick() {
        Log.d(TAG, "onClick")
        if (qsTile.state == Tile.STATE_INACTIVE && activeAccount != null) {
            login()
        }
    }

    private fun login() {
        try {
            webService.login(
                    this,
                    activeAccount!!.username,
                    activeAccount!!.password
            )
            changeStateLoading()
        } catch (e: Exception) {
            Crashlytics.logException(e)
        }
    }

    override fun onStopListening() {
        super.onStopListening()
        repoInterface.activeAccount.removeObserver(observer)
        Log.d(TAG, "onStopListening")
    }

//    override fun onDestroy() {
//        unregisterWifiReceiver()
//        super.onDestroy()
//        repoInterface.activeAccount.removeObserver(observer)
//        Log.d(TAG, "onDestroy")
//
//    }

//    private fun registerWifiReceiver() {
//        try {
//            val intentFilter = with(IntentFilter()) {
//                addAction("android.net.wifi.WIFI_SATE_CHANGED")
//                addAction("android.net.conn.CONNECTIVITY_CHANGE")
//                addAction("android.net.wifi.supplicant.CONNECTION_CHANGE")
//                addAction("android.net.wifi.STATE_CHANGE")
//                return@with this
//            }
//            registerReceiver(wifiReceiver, intentFilter)
//            Log.d(TAG, "WifiReceiver registered")
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.d(TAG, "WifiReceiver already registered")
//        }
//    }

//    private fun unregisterWifiReceiver() {
//        try {
//            unregisterReceiver(wifiReceiver)
//        } catch (e: Exception) {
//             already unregistered
//        }
//    }

//    override fun onWifiStateChanged(isConnectedToWifi: Boolean, ssid: String) {
//        updateState(isConnectedToWifi)
//    }

    override fun onLoggedListener(requestType: RequestType, isLogged: Boolean, responseString: String) {

        Log.d(TAG, "loginResponse")

        if (requestType == RequestType.LOGIN) {
            if (isLogged) {
                changeStateSuccess()
            } else {
                changeStateFailure()
            }
        }

        Handler().postDelayed({
//            updateState(true)
            changeStateNormal()
        }, 1500)
    }

//    private fun updateState(isWifiConnected: Boolean) {
//        with(qsTile) {
//
//            icon = Icon.createWithResource(this@OneClickTileService, R.drawable.ic_login)
//
//            if (isWifiConnected) {
//                state = Tile.STATE_INACTIVE
//                label = "Login"
//            } else {
//                state = Tile.STATE_UNAVAILABLE
//                label = "One Click"
//            }
//
//            updateTile()
//        }
//    }

    private fun changeStateNormal() = with(qsTile) {
        state = Tile.STATE_INACTIVE
        label = "Login"
        icon = Icon.createWithResource(
                this@OneClickTileService,
                R.drawable.ic_login
        )
        updateTile()
        Log.d(TAG, "stateNormal")
    }

    private fun changeStateLoading() = with(qsTile) {
        state = Tile.STATE_UNAVAILABLE
        label = "Loading"
        icon = Icon.createWithResource(
                this@OneClickTileService,
                R.drawable.ic_dots_horizontal
        )
        updateTile()
        Log.d(TAG, "stateLoading")
    }

    private fun changeStateSuccess() = with(qsTile) {
        state = Tile.STATE_ACTIVE
        label = "Success"
        icon = Icon.createWithResource(
                this@OneClickTileService,
                R.drawable.ic_done_white_48dp
        )
        updateTile()
        Log.d(TAG, "stateSuccess")
    }

    private fun changeStateFailure() = with(qsTile) {
        state = Tile.STATE_ACTIVE
        label = "Failed"
        icon = Icon.createWithResource(
                this@OneClickTileService,
                R.drawable.ic_error_outline_black_24dp
        )
        updateTile()
        Log.d(TAG, "stateFailure")
    }
}
