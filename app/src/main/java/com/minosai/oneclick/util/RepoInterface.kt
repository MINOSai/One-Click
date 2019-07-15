package com.minosai.oneclick.util

import androidx.lifecycle.MutableLiveData
import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.repo.OneClickRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoInterface @Inject constructor(val repo: OneClickRepo) {

    var activeAccount = MutableLiveData<AccountInfo>()
    //TODO: Change 'true' to 'false'
    var isAutoUpdateUsage: Boolean = false

    init {
        GlobalScope.launch {
            val account = repo.getActiveAccountFromDb()
            withContext(Dispatchers.Main) {
                activeAccount.value = account
            }
        }
        isAutoUpdateUsage = repo.isAutoUpdateUsage()
    }

    fun updateUsage(usage: String) = repo.updateUsage(usage)

    fun updateAccounts() = repo.fetchAccounts()

}