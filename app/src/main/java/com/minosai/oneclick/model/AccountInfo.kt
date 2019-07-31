package com.minosai.oneclick.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccountInfo (
        var username: String,
        var password: String,
        var usage: String,
        var renewalDate: String,
        var isActiveAccount: Boolean
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}