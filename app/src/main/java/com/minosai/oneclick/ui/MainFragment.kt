package com.minosai.oneclick.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.minosai.oneclick.R
import com.minosai.oneclick.di.Injectable
import com.minosai.oneclick.util.service.WebService
import com.minosai.oneclick.util.helper.Constants
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

class MainFragment : Fragment(), Injectable {

    @Inject
    lateinit var preferences: SharedPreferences

    @Inject
    lateinit var webService: WebService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        input_userid.setText(preferences.getString(Constants.PREF_USERNAME, ""))
        input_password.setText(preferences.getString(Constants.PREF_PASSWORD, ""))

        button_login.setOnClickListener {
            val userName = input_userid.text.toString()
            val password = input_password.text.toString()
            webService.login()
            saveUser(userName,  password)
        }

        button_logout.setOnClickListener { webService.logout() }
    }

    private fun saveUser(userName: String, password: String) {
        preferences.edit()
                .putString(Constants.PREF_USERNAME, userName)
                .putString(Constants.PREF_PASSWORD, password)
                .apply()
    }

}