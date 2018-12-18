package com.minosai.oneclick.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.content.SharedPreferences
import com.minosai.oneclick.db.OneClickDao
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.helper.PreferenceHelper.get
import com.minosai.oneclick.util.helper.PreferenceHelper.set
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class OneClickRepo @Inject constructor(val dao: OneClickDao, val preferences: SharedPreferences) {

    lateinit var allAccountInfo: LiveData<List<AccountInfo>>
    var liveActiveAccount: MutableLiveData<AccountInfo> = MutableLiveData()

    init {
        fetchAccounts()
    }

    private fun fetchAccounts() {
        allAccountInfo = dao.getAllAccounts()
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
    }

    fun setActiveUser(userName: String) {
        launch {
            dao.resetAccounts()
            dao.setActiveAccount(userName)
        }
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

    fun updateUsage(usage: String) {
        launch {
            val userName = getActiveAccount()?.username ?: ""
            try {
                dao.updateUsage(userName, usage)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setSessionLink(sessionLink: String?) {
        preferences[Constants.PREF_SESSION_LINK] = sessionLink
    }

    fun getSessionLink(): String? = preferences[Constants.PREF_SESSION_LINK]

    fun isAutoUpdateUsage(): Boolean {
        // TODO: Change default value to false
        return preferences[Constants.PREF_AUTOUPDATE_USAGE, true] ?: true
    }

    fun setAutoUpdateUsage(boolean: Boolean) {
        preferences[Constants.PREF_AUTOUPDATE_USAGE] = boolean
    }

    fun getActiveAccount(): AccountInfo? {
        allAccountInfo.value?.forEach { info ->
            if (info.isActiveAccount) {
                return info
            }
        }
        return null
    }

    fun getActiveAccountFromDb(): AccountInfo = dao.getActiveAccount()

    fun removeAccount(accountInfo: AccountInfo) = launch {
        dao.deleteAccounts(accountInfo)
    }

}