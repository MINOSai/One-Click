package com.minosai.oneclick.util.service

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.minosai.oneclick.repo.OneClickRepo
import com.minosai.oneclick.util.helper.Constants
import com.minosai.oneclick.util.receiver.listener.LoginLogoutListener
import com.minosai.oneclick.util.helper.PreferenceHelper.get
import kotlinx.coroutines.experimental.launch
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import javax.inject.Inject

class WebService @Inject constructor(val context: Context, val preferences: SharedPreferences, val repo: OneClickRepo) {

    companion object {
        enum class RequestType { LOGIN, LOGOUT }
    }

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    private var userName: String? = preferences[Constants.PREF_USERNAME]
    private var password: String? = preferences[Constants.PREF_PASSWORD]

    fun login(loginLogoutListener: LoginLogoutListener) {
        //TODO: Check if VOLSBB or VIT2.4G OR VIT5G
        val accountInfo = repo.activeAccount
        if (accountInfo != null) {
            Constants.URL_LOGIN.httpPost(listOf(
                    "userId" to accountInfo.username,
                    "password" to accountInfo.password,
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
                        setSessionLink(result.value)
                        loginLogoutListener.onLoggedListener(RequestType.LOGIN, true)
                    }
                }
            }
        } else {
            Toast.makeText(context, "No account found", Toast.LENGTH_SHORT).show()
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
                    repo.setSessionLink(null)
                }
            }
        }
    }

    fun setSessionLink(webpage: String) {
        if (webpage.contains("Check Your Account Details", true)) {
            val sessionLink = Jsoup.parse(webpage).getElementsByClass("orangeText10")[1].attr("href")
            Log.d(TAG, "HTML String : $sessionLink")
            repo.setSessionLink(sessionLink)
            val boolean = repo.isAutoUpdateUsage()
            if (boolean) {
                getUsage()
            }
        }
    }

    fun getUsage() {
        launch {
            try {
                val sessionLink = repo.getSessionLink()
                sessionLink?.let { link ->
                    val document = Jsoup.connect(link).get()
                    val subTexts: Elements = document.getElementsByClass("subTextRight")
                    val usageElement = subTexts[subTexts.size -1]
                    val usage: String = usageElement.child(0).text()
                    repo.updateUsage(usage)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}