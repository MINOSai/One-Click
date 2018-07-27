package com.minosai.oneclick.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.minosai.oneclick.model.AccountInfo

@Database(entities = [AccountInfo::class], version = 1, exportSchema = false)
abstract class OneClickDatabase : RoomDatabase() {

    abstract fun oneClickDao(): OneClickDao
    @Volatile lateinit var ISTANCNE: OneClickDatabase

}