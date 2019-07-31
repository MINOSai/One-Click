package com.minosai.oneclick.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.minosai.oneclick.model.AccountInfo

@Database(entities = [AccountInfo::class], version = 2, exportSchema = false)
abstract class OneClickDatabase : RoomDatabase() {

    abstract fun oneClickDao(): OneClickDao
    @Volatile lateinit var ISTANCNE: OneClickDatabase

}