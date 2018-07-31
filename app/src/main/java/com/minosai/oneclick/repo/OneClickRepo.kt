package com.minosai.oneclick.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.SharedPreferences
import android.database.sqlite.SQLiteConstraintException
import com.minosai.oneclick.db.OneClickDao
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.util.helper.Constants
import com.minosai.oneclick.util.helper.PreferenceHelper.get
import com.minosai.oneclick.util.helper.PreferenceHelper.set
import com.minosai.oneclick.util.service.WebService
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class OneClickRepo @Inject constructor(val dao: OneClickDao, val preferences: SharedPreferences) {

    lateinit var allAccountInfo: LiveData<PagedList<AccountInfo>>
    var activeAccount = MutableLiveData<AccountInfo>()

    init {
        fetchAccounts()
        refreshActiveAccount()
    }

    private fun fetchAccounts() {
        val pagedListConfig = PagedList.Config.Builder().setEnablePlaceholders(true)
                .setPrefetchDistance(20)
                .setPageSize(25)
                .build()
        allAccountInfo = LivePagedListBuilder(dao.getAllAccounts(), pagedListConfig)
                .build()
    }

    fun refreshActiveAccount() {
        launch {
            activeAccount.value = dao.getActiveAccount()
        }
    }

    fun addAccount(userName: String, password: String, usage: String, renewalDate: String, isActiveAccount: Boolean) {
        launch {
            dao.addAccount(AccountInfo(userName, password, usage, renewalDate, isActiveAccount))
        }
        if (isActiveAccount) {
            setUser(userName, password)
        }
    }

    fun setUser(userName: String, password: String) {

        preferences.edit()
                .putString(Constants.PREF_USERNAME, userName)
                .putString(Constants.PREF_PASSWORD, password)
                .apply()

        launch {
            dao.resetAccounts()
            dao.setActiveAccount(userName)
        }
        refreshActiveAccount()
    }

    fun changeFirstOpenBoolean() {
        preferences[Constants.PREF_IS_FIRST_TIME] = false
    }

    fun saveDisplayName(displayName: String) {
        preferences[Constants.PREF_DISPLAY_NAME] = displayName
    }

    fun getDisplayName(): String {
        return preferences[Constants.PREF_DISPLAY_NAME] ?: ""
    }

    fun updateUsage() {
//        activeAccount.value?.
    }

}