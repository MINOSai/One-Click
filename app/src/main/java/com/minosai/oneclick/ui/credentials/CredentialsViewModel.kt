package com.minosai.oneclick.ui.credentials

import androidx.lifecycle.ViewModel
import com.minosai.oneclick.repo.OneClickRepo
import javax.inject.Inject

class CredentialsViewModel @Inject constructor(val repo: OneClickRepo) : ViewModel() {

    fun addAccount(userName: String, password: String, isActiveAccount: Boolean) =
            repo.addAccount(userName, password, "", "", isActiveAccount)

    fun changeFirstOpenBoolean() = repo.changeFirstOpenBoolean()

    fun saveDisplayName(displayName: String) = repo.saveDisplayName(displayName)

}