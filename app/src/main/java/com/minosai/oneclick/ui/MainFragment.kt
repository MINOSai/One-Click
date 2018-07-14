package com.minosai.oneclick.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.minosai.oneclick.R
import com.minosai.oneclick.di.Injectable
import com.minosai.oneclick.model.Payload
import com.minosai.oneclick.network.WebService
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class MainFragment : Fragment(), Injectable {

    @Inject
    lateinit var webService: WebService
    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_login.setOnClickListener {
            val userName = input_userid.text.toString()
            val password = input_password.text.toString()
            login(userName, password)
        }

        button_logout.setOnClickListener { logout() }
    }

    private fun login(userName: String, password: String) {

        "http://phc.prontonetworks.com/cgi-bin/authlogin?URI=http://www.msftncsi.com/redirect".httpPost(
                listOf(
                        "userId" to userName,
                        "password" to password,
                        "serviceName" to "ProntoAuthentication",
                        "Submit22" to "Login"
                )
        ).responseString { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    Log.i("STATUS_LOGIN", "Did NOT log in")
                }
                is Result.Success -> {
                    Log.i("STATUS_LOGIN", "Logged in")
                }
            }
        }
    }

    private fun logout() {

        "http://phc.prontonetworks.com/cgi-bin/authlogout".httpGet().response { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    Log.i("STATUS_LOGOUT", "Did NOT Log out")
                }
                is Result.Success -> {
                    Log.i("STATUS_LOGOUT", "Logged out")
                }
            }
        }
    }

}