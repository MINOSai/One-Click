package com.minosai.oneclick.util.service

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.minosai.oneclick.repo.OneClickRepo
import com.minosai.oneclick.util.helper.Constants
import com.minosai.oneclick.util.helper.PreferenceHelper.get
import com.minosai.oneclick.util.helper.PreferenceHelper.set
import com.minosai.oneclick.util.receiver.listener.LoginLogoutListener
import kotlinx.coroutines.experimental.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import javax.inject.Inject

class WebService @Inject constructor(val context: Context, val preferences: SharedPreferences, val repo: OneClickRepo) {

    companion object {
        enum class RequestType { LOGIN, LOGOUT }
    }

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    private var userName = preferences.getString(Constants.PREF_USERNAME, "")
    private var password = preferences.getString(Constants.PREF_PASSWORD, "")

    init {
        userName = repo.activeAccount.value?.username
        password = repo.activeAccount.value?.password
    }

    fun login(loginLogoutListener: LoginLogoutListener) {
        //TODO: Check if VOLSBB or VIT2.4G OR VIT5G
        Constants.URL_LOGIN.httpPost(listOf(
                "userId" to userName,
                "password" to password,
                "serviceName" to "ProntoAuthentication",
                "Submit22" to "Login"
        )).responseString { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    Log.i(TAG, "Did NOT log in")
                    loginLogoutListener.onLoggedListener(RequestType.LOGIN, false)
                }
                is Result.Success -> {
                    Log.i(TAG, "Logged in")
                    Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show()
                    getSessionLink(result.value)
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

    fun getSessionLink(webpage: String) {
        if (webpage.contains("Check Your Account Details", true)) {
            val sessionLink = Jsoup.parse(webpage).getElementsByClass("orangeText10")[1].attr("href")
            Log.d(TAG, "HTML String : $sessionLink")
            preferences[Constants.PREF_SESSION_LINK] = sessionLink
            getUsage(sessionLink)
        }
    }

    fun getUsage(sessionLink: String) {
        launch {
            try {
                val document = Jsoup.connect(sessionLink).get()
                val subTexts: Elements = document.getElementsByClass("subTextRight")
                val usageElement: Element = subTexts[subTexts.size -1]
                val usage: String = usageElement.child(0).text()
                Log.d(TAG, "Usage GB: $usage")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}