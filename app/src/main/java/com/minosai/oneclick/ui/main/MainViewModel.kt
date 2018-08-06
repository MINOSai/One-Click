package com.minosai.oneclick.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.repo.OneClickRepo
import javax.inject.Inject

class MainViewModel @Inject constructor(val repo: OneClickRepo) : ViewModel() {

    var displayName: String = repo.getDisplayName()
    var isWifiConnected = false
    var isOnline = false
    var ssid = ""

    lateinit var activeAccount: AccountInfo

    init {
        this.activeAccount = repo.activeAccount
    }

    fun getLiveActiveAccount() = repo.liveActiveAccount

    fun refresh() = repo.refreshActiveAccount()

    fun getAllAccounts() = repo.allAccountInfo

}