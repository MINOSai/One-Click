package com.minosai.oneclick.di.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.minosai.oneclick.db.OneClickDao
import com.minosai.oneclick.db.OneClickDatabase
import com.minosai.oneclick.repo.OneClickRepo
import com.minosai.oneclick.util.service.WebService
import com.minosai.oneclick.util.helper.Constants
import com.minosai.oneclick.util.helper.PreferenceHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun providePreferences(application: Application): SharedPreferences = PreferenceHelper.defaultPrefs(application)

    @Provides
    @Singleton
    fun provideDatabase(application: Application): OneClickDatabase = Room.databaseBuilder(
            application,
            OneClickDatabase::class.java,
            Constants.DB_NAME
    ).build()

    @Provides
    @Singleton
    fun providesDao(database: OneClickDatabase): OneClickDao = database.oneClickDao()

    @Provides
    @Singleton
    fun provideRepo(dao: OneClickDao, preferences: SharedPreferences): OneClickRepo = OneClickRepo(dao, preferences)

    @Provides
    @Singleton
    fun provideWebService(application: Application, preferences: SharedPreferences, repo: OneClickRepo): WebService = WebService(application, preferences, repo)


}