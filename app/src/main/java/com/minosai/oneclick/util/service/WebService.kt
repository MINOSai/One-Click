package com.minosai.oneclick.util.service

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.minosai.oneclick.util.Constants
import kotlinx.coroutines.experimental.launch
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject

class WebService @Inject constructor(val context: Context, val preferences: SharedPreferences) {

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    fun login(userName: String, password: String) {
        Constants.URL_LOGIN.httpPost(listOf(
                "userId" to userName,
                "password" to password,
                "serviceName" to "ProntoAuthentication",
                "Submit22" to "Login"
        )).responseString { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    Log.i(TAG, "Did NOT log in")
                }
                is Result.Success -> {
                    Log.i(TAG, "Logged in")
                    Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show()
                    preferences.edit()
                            .putBoolean(Constants.PREF_ISLOGGED, true)
                            .putBoolean(Constants.PREF_ISONLINE, true)
                            .apply()
                }
            }
        }
    }

    fun logout() {
        Constants.URL_LOGOUT.httpGet().response { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    Log.i(TAG, "Did NOT Log out")
                }
                is Result.Success -> {
                    Log.i(TAG, "Logged out")
                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                    preferences.edit()
                            .putBoolean(Constants.PREF_ISLOGGED, false)
                            .putBoolean(Constants.PREF_ISONLINE, false)
                            .apply()
                }
            }
        }
    }

    fun isOnline() {
        launch {
            try {
                Thread.sleep(500)
                val timeoutMs = 1500
                val sock = Socket()
                val sockaddr = InetSocketAddress("8.8.8.8", 53)

                sock.connect(sockaddr, timeoutMs)
                sock.close()

                setOnlineState(true)
            } catch (e: IOException) {
                setOnlineState(false)
            }
        }
    }

    private fun setOnlineState(isOnline: Boolean) {
        Log.i(TAG, "isOnline : $isOnline")
        preferences.edit()
                .putBoolean(Constants.PREF_ISONLINE, isOnline)
                .apply()
    }

}