package com.minosai.oneclick.util.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.minosai.oneclick.network.WebService
import com.minosai.oneclick.network.WebService.Companion.RequestType
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.RepoInterface
import com.minosai.oneclick.util.getSSID
import com.minosai.oneclick.util.listener.LoginLogoutListener
import com.minosai.oneclick.util.receiver.WifiReceiver.Companion.SSID_LIST
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

        // TODO: start widget loading
        context?.let {
            isWifiConnected(it)
        }
    }

    override fun onLoggedListener(requestType: RequestType, isLogged: Boolean, responseString: String) {
        //TODO: stop widget loading
        if (requestType == RequestType.LOGIN && isLogged && repoInterface.isAutoUpdateUsage) {
            webService.getUsage {  usage ->
                repoInterface.updateUsage(usage)
            }
        }
    }

    private fun isWifiConnected(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info: NetworkInfo = connectivityManager.activeNetworkInfo
        if (info.isConnected) {
            val ssid = info.extraInfo ?: context.getSSID()
            if (ssid != null && ssid in SSID_LIST) {
                checkInternet()
            } else {
                checkProntoNetworks(info.extraInfo)
            }
        }
    }

    private fun checkProntoNetworks(ssid: String?) {
        "http://phc.prontonetworks.com/".httpGet().response { request, response, result ->
            if (result is Result.Success) {
                checkInternet()
            }
        }
    }

    private fun checkInternet() {
//        "https://www.example.com".httpGet().timeout(500).response { _, _, result ->
//            when(result) {
//                is Result.Failure -> {
//                    webService.login(this, repoInterface.activeAccount.username, repoInterface.activeAccount.password)
//                }
//                is Result.Success -> {
//                    webService.logout(this)
//                }
//            }
//        }
    }
}