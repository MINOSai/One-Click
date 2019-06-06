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
import com.minosai.oneclick.R
import com.minosai.oneclick.network.WebService
import com.minosai.oneclick.network.WebService.Companion.RequestType
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.RepoInterface
import com.minosai.oneclick.util.helper.LoginLogoutBroadcastHelper
import com.minosai.oneclick.util.listener.LoginLogoutListener
import com.minosai.oneclick.util.listener.WifiConnectivityListener
import com.minosai.oneclick.util.receiver.LoginLogoutReceiver
import com.minosai.oneclick.util.receiver.WifiReceiver
import dagger.android.AndroidInjection
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
@TargetApi(Build.VERSION_CODES.N)
class OneClickTileService : TileService(),
        WifiConnectivityListener,
        LoginLogoutListener {

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var webService: WebService
    @Inject
    lateinit var repoInterface: RepoInterface

    private lateinit var wifiReceiver: WifiReceiver
    private lateinit var loginLogoutReceiver: LoginLogoutReceiver

    private var isWifiConnected = false
    private var isOnline = false


    override fun onCreate() {
        super.onCreate()

        AndroidInjection.inject(this)

        wifiReceiver = WifiReceiver(this)
//        loginLogoutReceiver = LoginLogoutReceiver(this)

        registerWifiReceiver()
//        registerLoginLogoutReceiver()

        repoInterface.updateAccounts()
    }

//    override fun onStartListening() {
//        super.onStartListening()
///*        try {
//            mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance()
//            mInternetAvailabilityChecker.addInternetConnectivityListener(this)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }*/
//    }

    override fun onClick() {
        // TODO: if no account found open app
//        when(qsTile.state) {
//            Tile.STATE_INACTIVE -> webService.login(this, repoInterface.activeAccount.username, rfepoInterface.activeAccount.password)
//            Tile.STATE_ACTIVE -> webService.logout(this)
//        }
        if (qsTile.state != Tile.STATE_UNAVAILABLE) {
            unlockAndRun {
//                repoInterface
//                val dialog = OneClickDialogClass(applicationContext, webService, repoInterface.activeAccount)
//                showDialog(dialog)
                webService.login(this, repoInterface.activeAccount.username, repoInterface.activeAccount.password)
                changeStateLoading()
            }
        }
    }

//    override fun onStopListening() {
////        try {
////            mInternetAvailabilityChecker.removeInternetConnectivityChangeListener(this)
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
//        super.onStopListening()
//    }

    override fun onDestroy() {
        unregisterWifiReceiver()
//        unregisterLoginLogoutReceiver()
        super.onDestroy()
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

    private fun registerLoginLogoutReceiver() {
        try {
            val intentFilter = IntentFilter()
            intentFilter.addAction(LoginLogoutBroadcastHelper.LOGIN_LOGOUT_ACTION)
            registerReceiver(loginLogoutReceiver, intentFilter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun unregisterLoginLogoutReceiver() {
        try {
            unregisterReceiver(loginLogoutReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onWifiStateChanged(isConnectedToWifi: Boolean, ssid: String) {
        updateState(isConnectedToWifi)
    }

//    //TODO move this to dialog
//    override fun onLoggedListener(requestType: RequestType, isLogged: Boolean, responseString: String) {
//        isOnline = isLogged
//        when(requestType) {
//
//            RequestType.LOGIN -> {
//                isOnline = isLogged
//                if (isLogged) {
//                    if (repoInterface.isAutoUpdateUsage) {
//                        webService.getUsage { usage ->
//                            repoInterface.updateUsage(usage)
//                        }
//                    }
//                }
//            }
//
//            RequestType.LOGOUT -> {
//                if (isOnline && isLogged) {
//                    isOnline = false
//                }
//            }
//        }
//        updateState(true)
//    }

    override fun onLoggedListener(requestType: RequestType, isLogged: Boolean, responseString: String) {

        if (requestType == RequestType.LOGIN) {
            if (isLogged) {
                if (repoInterface.isAutoUpdateUsage) {
                    webService.getUsage { usage ->
                        repoInterface.updateUsage(usage)
                    }
                }
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
//        if (isWifiConnected) {
//            if (isOnline) changeStateLogout() else changeStateLogin()
//        } else {
//            changeStateDisable()
//        }
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
        label = "Succuess"
        icon = Icon.createWithResource(this@OneClickTileService, R.drawable.ic_done_white_48dp)
        updateTile()
    }

    private fun changeStateFailure() = with(qsTile) {
        state = Tile.STATE_ACTIVE
        label = "Failed"
        icon = Icon.createWithResource(this@OneClickTileService, R.drawable.ic_error_outline_black_24dp)
        updateTile()
    }

//    private fun changeStateDisable() = with(qsTile) {
//        state = Tile.STATE_UNAVAILABLE
//        updateTile()
//    }
//
//    private fun changeStateEnable() = with(qsTile) {
//        state = Tile.STATE_INACTIVE
//        updateTile()
//    }
//
//    private fun changeStateLogin() = with(qsTile) {
//        state = Tile.STATE_INACTIVE
//        icon = Icon.createWithResource(this@OneClickTileService, R.drawable.ic_login)
//        label = "Login"
//        updateTile()
//    }
//
//    private fun changeStateLogout() = with(qsTile) {
//        state = Tile.STATE_ACTIVE
//        icon = Icon.createWithResource(this@OneClickTileService, R.drawable.ic_logout)
//        label = "Logout"
//        updateTile()
//    }
}
