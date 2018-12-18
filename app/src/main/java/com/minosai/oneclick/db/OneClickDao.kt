package com.minosai.oneclick.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.minosai.oneclick.model.AccountInfo

@Dao
interface OneClickDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccount(accountInfo: AccountInfo)

    @Query("SELECT * FROM accountinfo ORDER BY isActiveAccount DESC")
    fun getAllAccounts(): LiveData<List<AccountInfo>>

    @Query("SELECT * FROM accountinfo WHERE isactiveaccount= '1'")
    fun getActiveAccount(): AccountInfo

    @Query("UPDATE accountinfo SET isActiveAccount = '0' WHERE 1=1")
    fun resetAccounts()

    @Query("UPDATE accountinfo SET isActiveAccount = '1' WHERE username LIKE :userName")
    fun setActiveAccount(userName: String)

    @Query("SELECT * FROM accountinfo WHERE username LIKE :userName")
    fun getAccoutByUserName(userName: String): LiveData<AccountInfo>

    @Query("UPDATE accountinfo SET usage=:usage WHERE  username LIKE :userName")
    fun updateUsage(userName: String, usage: String)

    @Query("UPDATE accountinfo SET password=:password WHERE username LIKE :userName")
    fun updatePassword(userName: String, password: String)

    @Query("UPDATE accountinfo SET renewaldate=:date WHERE username LIKE :userName")
    fun updateRenewalDate(userName: String, date: String)

    @Delete
    fun deleteAccounts(vararg accountInfo: AccountInfo)
}