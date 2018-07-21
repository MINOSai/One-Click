package com.minosai.oneclick.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.IntentFilter
import android.content.SharedPreferences
import android.util.Log
import android.widget.RemoteViews
import com.minosai.oneclick.R
import com.minosai.oneclick.util.helper.Constants
import com.minosai.oneclick.util.helper.LoginLogoutBroadcastHelper
import com.minosai.oneclick.util.receiver.LoginLogoutReceiver
import com.minosai.oneclick.util.receiver.WifiReceiver
import com.minosai.oneclick.util.receiver.listener.LoginLogoutListener
import com.minosai.oneclick.util.receiver.listener.WifiConnectivityListener
import com.minosai.oneclick.util.service.WebService
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker
import com.treebo.internetavailabilitychecker.InternetConnectivityListener

/**
 * Implementation of App Widget functionality.
 */
class OneClickWidget :
        AppWidgetProvider(),
        InternetConnectivityListener,
        WifiConnectivityListener,
        LoginLogoutListener {

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    private lateinit var wifiReceiver: WifiReceiver
    private lateinit var loginLogoutReceiver: LoginLogoutReceiver
    private lateinit var mInternetAvailabilityChecker: InternetAvailabilityChecker

    private var isWifiConnected = false
    private var isOnline = false

    private lateinit var preferences: SharedPreferences
    private lateinit var webService: WebService

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        preferences = context.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE)
        webService = WebService(context, preferences)

        wifiReceiver = WifiReceiver(this)
        loginLogoutReceiver = LoginLogoutReceiver(this)

        registerWifiReceiver(context)

        InternetAvailabilityChecker.init(context)
        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance()
        mInternetAvailabilityChecker.addInternetConnectivityListener(this)
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d("Widget", "onDisabled")
        mInternetAvailabilityChecker.removeInternetConnectivityChangeListener(this)
        unregisterWifiReceiver(context)
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

//            val widgetText = context.getString(R.string.appwidget_text)
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.one_click_widget)
//            views.setTextViewText(R.id.fab_widget, widgetText)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }


    private fun registerWifiReceiver(context: Context) {
        try {
            val intentFilter = IntentFilter()
            with(intentFilter) {
                addAction("android.net.wifi.WIFI_SATE_CHANGED")
                addAction("android.net.conn.CONNECTIVITY_CHANGE")
                addAction("android.net.wifi.supplicant.CONNECTION_CHANGE")
                addAction("android.net.wifi.STATE_CHANGE")
            }
            context.registerReceiver(wifiReceiver, intentFilter)
            Log.d(TAG, "WifiReceiver registered")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "WifiReceiver already registered")
        }
    }

    private fun unregisterWifiReceiver(context: Context) {
        try {
            context.unregisterReceiver(wifiReceiver)
        } catch (e: Exception) {
            // already unregistered
        }
    }

    private fun registerLoginLogoutReceiver(context: Context) {
        try {
            val intentFilter = IntentFilter()
            intentFilter.addAction(LoginLogoutBroadcastHelper.LOGIN_LOGOUT_ACTION)
            context.registerReceiver(loginLogoutReceiver, intentFilter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun unregisterLoginLogoutReceiver(context: Context) {
        try {
            context.unregisterReceiver(loginLogoutReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onInternetConnectivityChanged(isConnected: Boolean) {
        isOnline = isConnected
        updateState()
    }

    override fun onWifiStateChanged(isConnectedToWifi: Boolean) {
        isWifiConnected = isConnectedToWifi
        updateState()
    }

    override fun onLoggedListener(requestType: WebService.Companion.RequestType, isLogged: Boolean) {
        isOnline = isLogged
        when(requestType) {
            WebService.Companion.RequestType.LOGIN -> {
                isOnline = isLogged
            }
            WebService.Companion.RequestType.LOGOUT -> {
                if (isOnline && isLogged) {
                    isOnline = false
                }
            }
        }
        updateState()
    }


    private fun updateState() {
        Log.d(TAG, "widget - isOnline: $isOnline - isWifiConnected: $isWifiConnected")
    }

}

