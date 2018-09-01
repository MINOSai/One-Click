package com.minosai.oneclick.util.listener

import com.minosai.oneclick.util.service.WebService
import com.minosai.oneclick.util.service.WebService.Companion.RequestType

interface LoginLogoutListener {

    fun onLoggedListener(requestType: RequestType, isLogged: Boolean)

}