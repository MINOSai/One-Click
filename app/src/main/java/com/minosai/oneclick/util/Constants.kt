package com.minosai.oneclick.util

object Constants {

    const val PACKAGE_NAME = "com.minosai.oneclick"
    const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=$PACKAGE_NAME"
    const val PREF_KEY = "$PACKAGE_NAME.pref"

    const val PREF_USERNAME = "$PACKAGE_NAME.pref.username"

    const val PREF_PASSWORD = "$PACKAGE_NAME.pref.password"
    const val PREF_IS_FIRST_TIME = "$PACKAGE_NAME.pref.isfirsttime"
    const val PREF_DISPLAY_NAME = "display_name"
    //    const val PREF_SESSION_LINK = "$PACKAGE_NAME.pref.sessionlink"
    const val PREF_AUTOUPDATE_USAGE = "$PACKAGE_NAME.pref.autoupdateusage"
    const val PREF_LOGIN_APP_START = "auto_login_app_start"
    const val PREF_LOGIN_QS_TILE = "auto_login_quicktile"
    const val PREF_DARK_THEME = "dark_theme"
    const val PREF_OPENED_INFO = "$PACKAGE_NAME.pref.opened_info"

    const val EXTRA_TYPE = "$PACKAGE_NAME.extra.REQUEST_TYPE"

    const val URL_LOGIN = "http://phc.prontonetworks.com/cgi-bin/authlogin"
    const val URL_LOGOUT = "http://phc.prontonetworks.com/cgi-bin/authlogout"

    const val DB_NAME = "oneclickdatabase.db"

    const val REQUEST_TIMEOUT = 5000

    const val LOGIN_LOGOUT_ACTION = "$PACKAGE_NAME.action.LOGIN_LOGOUT_ACTION"

    enum class SheetAction {
        NEW_ACCOUNT, INCOGNITO, EDIT_ACCOUNT
    }

    enum class AccountAction {
        COPY_PASSWORD, VIEW_PASSWORD, EDIT_ACCOUNT, DELETE_ACCOUNT, SET_PRIMARY, LOGIN
    }

    object Response {
        const val LOGIN_SUCCESS = "Successful Pronto Authentication"
        const val LOGIN_ALREADY = "Already logged in"
        const val LOGIN_INVALID_CREDS1 = "please check your username and password and try again"
        const val LOGIN_INVALID_CREDS2 = "password was not accepted"
        const val LOGIN_NO_ACCOUNT = "that account does not exist"

        const val LOGOUT_SUCCESS = "Logout successful"
        const val LOGOUT_ALREADY = "No active session"
    }

}