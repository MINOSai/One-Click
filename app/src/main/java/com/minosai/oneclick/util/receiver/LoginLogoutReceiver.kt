package com.minosai.oneclick.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.minosai.oneclick.repo.OneClickRepo
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.RepoInterface
import com.minosai.oneclick.util.receiver.WifiReceiver.Companion.SSID_LIST
import com.minosai.oneclick.util.service.WebService.Companion.RequestType
import com.minosai.oneclick.util.receiver.listener.LoginLogoutListener
import com.minosai.oneclick.util.service.WebService
import dagger.android.AndroidInjection
import javax.inject.Inject

class LoginLogoutReceiver : BroadcastReceiver(), LoginLogoutListener {

    @Inject
    lateinit var preferences: SharedPreferences
    @Inject
    lateinit var webService: WebService
    @Inject
    lateinit var repoInterface: RepoInterface

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    override fun onReceive(context: Context?, intent: Intent?) {

        AndroidInjection.inject(this, context)

        context?.let {
            isWifiConnected(it)
        }
    }

    override fun onLoggedListener(requestType: WebService.Companion.RequestType, isLogged: Boolean) {
        if (requestType == RequestType.LOGIN && isLogged && repoInterface.isAutoUpdateUsage) {
            webService.getUsage {  usage ->
                repoInterface.updateUsage(usage)
            }
        }
    }

    private fun isWifiConnected(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info: NetworkInfo = connectivityManager.activeNetworkInfo
        if (info.isConnected && info.extraInfo in SSID_LIST) {
            checkInternet()
        }
    }

    private fun checkInternet() {
        "https://www.example.com".httpGet().timeout(500).response { _, _, result ->
            when(result) {
                is Result.Failure -> {
                    webService.login(this, repoInterface.activeAccount)
                }
                is Result.Success -> {
                    webService.logout(this)
                }
            }
        }
    }

}