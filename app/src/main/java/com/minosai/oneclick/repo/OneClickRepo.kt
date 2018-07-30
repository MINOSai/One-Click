package com.minosai.oneclick.repo

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.SharedPreferences
import android.database.sqlite.SQLiteConstraintException
import com.minosai.oneclick.db.OneClickDao
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.util.helper.Constants
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class OneClickRepo @Inject constructor(val dao: OneClickDao, val preferences: SharedPreferences) {

    lateinit var allAccountInfo: LiveData<PagedList<AccountInfo>>

    init {
        fetchAccounts()
    }

    private fun fetchAccounts() {
        val pagedListConfig = PagedList.Config.Builder().setEnablePlaceholders(true)
                .setPrefetchDistance(20)
                .setPageSize(25)
                .build()
        allAccountInfo = LivePagedListBuilder(dao.getAllAccounts(), pagedListConfig)
                .build()
    }

    fun addAccount(userName: String, password: String, isActiveAccount: Boolean) {
        launch {
            try {
                dao.addAccount(AccountInfo(userName, password, isActiveAccount))
            } catch (e: SQLiteConstraintException) {
                e.printStackTrace()
            }
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
    }

    fun changeFirstOpenBoolean() {
        preferences.edit()
                .putBoolean(Constants.PREF_IS_FIRST_TIME, false)
                .apply()
    }

    fun saveDisplayName(displayName: String) {
        preferences.edit()
                .putString(Constants.PREF_DISPLAY_NAME, displayName)
                .apply()
    }

    fun getDisplayName(): String = preferences.getString(Constants.PREF_DISPLAY_NAME, "")

}