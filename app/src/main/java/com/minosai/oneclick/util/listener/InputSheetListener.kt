package com.minosai.oneclick.util.listener

import com.minosai.oneclick.ui.main.MainFragment
import com.minosai.oneclick.util.Constants

interface InputSheetListener {

    fun onSheetResponse(userName: String, password: String, isActiveAccount: Boolean, action: Constants.SheetAction)

}