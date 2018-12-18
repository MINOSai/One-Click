package com.minosai.oneclick.ui.main

import androidx.lifecycle.ViewModel
import android.content.Context
import android.view.View
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.repo.OneClickRepo
import com.minosai.oneclick.util.Constants
import javax.inject.Inject

class MainViewModel @Inject constructor(val repo: OneClickRepo) : ViewModel() {

    var displayName: String = repo.getDisplayName()
    var isWifiConnected = false
    var isOnline = false
    var ssid = ""

    var state = Constants.ButtonAction.CONNECT
    lateinit var context: Context
    lateinit var view: View

    fun getLiveActiveAccount() = repo.liveActiveAccount

    fun getAllAccounts() = repo.allAccountInfo

    fun getActiveAccount() = repo.getActiveAccount()

    fun isAutoUpdateUsage() = repo.isAutoUpdateUsage()

    fun updateUsage(usage: String) = repo.updateUsage(usage)

    fun addUser(userName: String, password: String, isActiveAccount: Boolean) = repo.addAccount(userName, password, "", "", isActiveAccount)

    fun setPrimaryAccount(userName: String) = repo.setActiveUser(userName)

    fun removeAccount(accountInfo: AccountInfo) = repo.removeAccount(accountInfo)

}