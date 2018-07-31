package com.minosai.oneclick.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class AccountInfo (
        @PrimaryKey var username: String,
        var password: String,
        var usage: String,
        var renewalDate: String,
        var isActiveAccount: Boolean
)