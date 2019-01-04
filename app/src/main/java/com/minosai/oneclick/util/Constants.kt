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

    enum class SheetAction {
        NEW_ACCOUNT, INCOGNITO, EDIT_ACCOUNT
    }

}