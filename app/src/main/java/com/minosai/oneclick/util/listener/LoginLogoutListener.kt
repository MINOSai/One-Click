package com.minosai.oneclick.util.listener

import com.minosai.oneclick.network.WebService.Companion.RequestType

interface LoginLogoutListener {

    fun onLoggedListener(requestType: RequestType, isLogged: Boolean)

}