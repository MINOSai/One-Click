package com.minosai.oneclick.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class AccountInfo(
        @PrimaryKey val username: String,
        val password: String
)