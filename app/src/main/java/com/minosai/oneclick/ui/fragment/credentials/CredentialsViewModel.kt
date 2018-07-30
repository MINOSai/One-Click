package com.minosai.oneclick.ui.fragment.credentials

import android.arch.lifecycle.ViewModel
import com.minosai.oneclick.repo.OneClickRepo
import javax.inject.Inject

class CredentialsViewModel @Inject constructor(val repo: OneClickRepo) : ViewModel() {

    fun addAccount(userName: String, password: String, isActiveAccount: Boolean) = repo.addAccount(userName, password, isActiveAccount)

    fun changeFirstOpenBoolean() = repo.changeFirstOpenBoolean()

    fun saveDisplayNamøe(displayName: String) = repo.saveDisplayName(displayName)

}