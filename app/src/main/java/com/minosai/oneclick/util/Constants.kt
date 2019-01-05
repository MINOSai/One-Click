package com.minosai.oneclick.util

object Constants {

    val PACKAGE_NAME = "com.minosai.oneclick"

    val PREF_KEY = "$PACKAGE_NAME.pref"

    val PREF_USERNAME = "$PACKAGE_NAME.pref.username"
    val PREF_PASSWORD = "$PACKAGE_NAME.pref.password"
    val PREF_IS_FIRST_TIME = "$PACKAGE_NAME.pref.isfirsttime"
    val PREF_DISPLAY_NAME = "display_name"
    val PREF_SESSION_LINK = "$PACKAGE_NAME.pref.sessionlink"
    val PREF_AUTOUPDATE_USAGE = "$PACKAGE_NAME.pref.autoupdateusage"

    val URL_LOGIN = "http://phc.prontonetworks.com/cgi-bin/authlogin"
    val URL_LOGOUT = "http://phc.prontonetworks.com/cgi-bin/authlogout"

    val DB_NAME = "oneclickdatabase.db"

    val REQUEST_TIMEOUT = 5000

    enum class SheetAction {
        NEW_ACCOUNT, INCOGNITO, EDIT_ACCOUNT
    }

    object Response {
        val LOGIN_SUCCESS = "Successful Pronto Authentication"
        val LOGIN_ALREADY = "Already logged in"
        val LOGIN_INVALID_CREDS1 = "please check your username and password and try again"
        val LOGIN_INVALID_CREDS2 = "password was not accepted"
        val LOGIN_NO_ACCOUNT = "that account does not exist"

        val LOGOUT_SUCCESS = "Logout successful"
        val LOGOUT_ALREADY = "No active session"
    }

}