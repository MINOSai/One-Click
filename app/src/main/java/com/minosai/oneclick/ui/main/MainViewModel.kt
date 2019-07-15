package com.minosai.oneclick.ui.main

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.model.UserPrefs
import com.minosai.oneclick.repo.OneClickRepo
import javax.inject.Inject

class MainViewModel @Inject constructor(val repo: OneClickRepo) : ViewModel() {

    var displayName: String = repo.getDisplayName()
    var userPrefs = UserPrefs()
    val isLoading = MutableLiveData<Boolean>()

    lateinit var context: Context
    lateinit var view: View

    fun getLiveActiveAccount() = repo.liveActiveAccount

    fun getAllAccounts() = repo.allAccountInfo

    fun updateUserPrefs() {
        userPrefs = repo.getUserPrefs()
    }

    fun getActiveAccount() = repo.getActiveAccount()

    fun isAutoUpdateUsage() = repo.isAutoUpdateUsage()

    fun updateUsage(usage: String) = repo.updateUsage(usage)

    fun addUser(userName: String, password: String, isActiveAccount: Boolean) = repo.addAccount(userName, password, "", "", isActiveAccount)

    fun setPrimaryAccount(userName: String) = repo.setActiveUser(userName)

    fun removeAccount(accountInfo: AccountInfo) = repo.removeAccount(accountInfo)

    fun updateAccInfo(accountInfo: AccountInfo) {
        repo.updateAccountInfo(accountInfo)
    }

    fun startLoading() {
        isLoading.value = true
    }

    fun stopLoading() {
        isLoading.value = false
    }
}