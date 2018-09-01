package com.minosai.oneclick.util.listener

interface NewUserSheetListener {

    fun onAddNewUser(userName: String, password: String, isActiveAccount: Boolean)

}