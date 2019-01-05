package com.minosai.oneclick.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.helper.PreferenceHelper.get
import com.minosai.oneclick.util.helper.PreferenceHelper.set
import com.minosai.oneclick.util.listener.LoginLogoutListener
import kotlinx.coroutines.experimental.launch
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import javax.inject.Inject

class WebService @Inject constructor(val context: Context, val preferences: SharedPreferences) {

    companion object {
        enum class RequestType { LOGIN, LOGOUT }
//        val usageWork: OneTimeWorkRequest = OneTimeWorkRequestBuilder<UsageWorker>().build()
    }

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    fun login(loginLogoutListener: LoginLogoutListener, userName: String?, password: String?) {
        //TODO: Check if VOLSBB or VIT2.4G OR VIT5G
        if (userName != null && password != null) {
            Constants.URL_LOGIN.httpPost(listOf(
                    "userId" to userName,
                    "password" to password,
                    "serviceName" to "ProntoAuthentication",
                    "Submit22" to "Login"
            )).responseString { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        Log.i(TAG, "Did NOT log in")
                        loginLogoutListener.onLoggedListener(RequestType.LOGIN, false, "Network failure")
                    }
                    is Result.Success -> {
                        Log.i(TAG, "Logged in")
//                        Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show()
                        if (result.value.contains(Constants.Response.LOGIN_SUCCESS, true)) {
                            setSessionLink(result.value)
                            loginLogoutListener.onLoggedListener(RequestType.LOGIN, true, Constants.Response.LOGIN_SUCCESS)
                        } else if (result.value.contains(Constants.Response.LOGIN_ALREADY, true)) {
                            loginLogoutListener.onLoggedListener(RequestType.LOGIN, true, Constants.Response.LOGIN_ALREADY)
                        } else if (result.value.contains(Constants.Response.LOGIN_INVALID_CREDS1, true)
                                || result.value.contains(Constants.Response.LOGIN_INVALID_CREDS2, true)
                                || result.value.contains(Constants.Response.LOGIN_NO_ACCOUNT, true)) {
                            loginLogoutListener.onLoggedListener(RequestType.LOGIN, false, "Invalid credentials")
                        } else {
                            loginLogoutListener.onLoggedListener(RequestType.LOGIN, false, "Login failure")
                        }
                    }
                }
            }
        } else {
//            Toast.makeText(context, "No account found", Toast.LENGTH_SHORT).show()
        }
    }

    fun logout(loginLogoutListener: LoginLogoutListener) {
        Constants.URL_LOGOUT.httpGet().responseString { _, _, result ->
            when (result) {
                is Result.Failure -> {
                    Log.i(TAG, "Did NOT Log out")
                    loginLogoutListener.onLoggedListener(RequestType.LOGOUT, false, "Network failure")
                }
                is Result.Success -> {
                    Log.i(TAG, "Logged out")
//                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                    if (result.value.contains(Constants.Response.LOGOUT_SUCCESS, true)) {
                        loginLogoutListener.onLoggedListener(RequestType.LOGOUT, true, Constants.Response.LOGOUT_SUCCESS)
                    } else if (result.value.contains(Constants.Response.LOGOUT_ALREADY, true)) {
                        loginLogoutListener.onLoggedListener(RequestType.LOGOUT, false, Constants.Response.LOGOUT_ALREADY)
                    } else {
                        loginLogoutListener.onLoggedListener(RequestType.LOGOUT, false, Constants.Response.LOGOUT_ALREADY)
                    }
                    preferences[Constants.PREF_SESSION_LINK] = null
                }
            }
        }
    }

    fun setSessionLink(webpage: String): String? {
        if (webpage.contains("Check Your Account Details", true)) {
            val sessionLink = Jsoup.parse(webpage).getElementsByClass("orangeText10")[1].attr("href")
            Log.d(TAG, "HTML String : $sessionLink")
            preferences[Constants.PREF_SESSION_LINK] = sessionLink
            return sessionLink
        } else {
            return null
        }
    }

    fun startUsageWorker() {
//        WorkManager.getInstance().enqueue(usageWork)
    }

    fun getUsage(updateUsage: (usage: String) -> Unit) {
        launch {
            try {
                val sessionLink: String? = preferences[Constants.PREF_SESSION_LINK]
                sessionLink?.let { link ->
                    val document = Jsoup.connect(link).get()
                    val subTexts: Elements = document.getElementsByClass("subTextRight")
                    val usageElement = subTexts[subTexts.size -1]
                    val usage: String = usageElement.child(0).text()
                    updateUsage(usage)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}