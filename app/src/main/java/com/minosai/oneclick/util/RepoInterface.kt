package com.minosai.oneclick.util

import com.minosai.oneclick.model.AccountInfo
import com.minosai.oneclick.repo.OneClickRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepoInterface @Inject constructor(val repo: OneClickRepo) {

    lateinit var activeAccount: AccountInfo
    //TODO: Change 'true' to 'false'
    var isAutoUpdateUsage: Boolean = true

    init {
        GlobalScope.launch {
            activeAccount = repo.getActiveAccountFromDb()
        }
        isAutoUpdateUsage = repo.isAutoUpdateUsage()
    }

    fun updateUsage(usage: String) = repo.updateUsage(usage)

    fun updateAccounts() = repo.fetchAccounts()

}