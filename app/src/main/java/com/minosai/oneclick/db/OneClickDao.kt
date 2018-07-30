package com.minosai.oneclick.db

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.minosai.oneclick.model.AccountInfo

@Dao
interface OneClickDao {

    @Insert
    fun addAccount(accountInfo: AccountInfo)

    @Query("SELECT * FROM accountinfo")
    fun getAllAccounts(): DataSource.Factory<Int, AccountInfo>

    @Query("UPDATE accountinfo SET isactiveaccount='false' WHERE 1=1")
    fun resetAccounts()

    @Query("UPDATE accountinfo SET isactiveaccount='true' WHERE username LIKE :userName")
    fun setActiveAccount(userName: String)

}