package com.minosai.oneclick.model

data class UserPrefs (
        var displayName: String = "VITian",
        var loginAppStart: Boolean = false,
        var loginQsTile:Boolean = false,
        var autoRefresh: Boolean = false
)