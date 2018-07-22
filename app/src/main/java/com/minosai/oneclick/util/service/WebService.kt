package com.minosai.oneclick.util.service

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.minosai.oneclick.util.helper.Constants
import com.minosai.oneclick.util.receiver.listener.LoginLogoutListener
import javax.inject.Inject

class WebService @Inject constructor(val context: Context, val preferences: SharedPreferences) {

    companion object {
        enum class RequestType { LOGIN, LOGOUT }
    }

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    private var userName = preferences.getString(Constants.PREF_USERNAME, "")
    private var password = preferences.getString(Constants.PREF_PASSWORD, "")

    fun login(loginLogoutListener: LoginLogoutListener) {
        //TODO: Check if VOLSBB or VIT2.4G OR VIT5G
        Constants.URL_LOGIN.httpPost(listOf(
                "userId" to userName,
                "password" to password,
                "serviceName" to "ProntoAuthentication",
                "Submit22" to "Login"
        )).responseString { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    Log.i(TAG, "Did NOT log in")
                    loginLogoutListener.onLoggedListener(RequestType.LOGIN, false)
                }
                is Result.Success -> {
                    Log.i(TAG, "Logged in")
                    Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show()
                    loginLogoutListener.onLoggedListener(RequestType.LOGIN, true)
                }
            }
        }
    }

    fun logout(loginLogoutListener: LoginLogoutListener) {
        Constants.URL_LOGOUT.httpGet().response { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    Log.i(TAG, "Did NOT Log out")
                    loginLogoutListener.onLoggedListener(RequestType.LOGOUT, false)
                }
                is Result.Success -> {
                    Log.i(TAG, "Logged out")
                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                    loginLogoutListener.onLoggedListener(RequestType.LOGOUT, true)
                }
            }
        }
    }

}