package com.minosai.oneclick.di.module

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.minosai.oneclick.db.OneClickDao
import com.minosai.oneclick.db.OneClickDatabase
import com.minosai.oneclick.network.WebService
import com.minosai.oneclick.repo.OneClickRepo
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.RepoInterface
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
    fun provideWebService(application: Application, preferences: SharedPreferences): WebService = WebService(application/*, preferences*/)

    @Provides
    @Singleton
    fun provideDatabase(application: Application): OneClickDatabase = Room.databaseBuilder(
            application,
            OneClickDatabase::class.java,
            Constants.DB_NAME
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providesDao(database: OneClickDatabase): OneClickDao = database.oneClickDao()

    @Provides
    @Singleton
    fun provideRepo(dao: OneClickDao, preferences: SharedPreferences): OneClickRepo = OneClickRepo(dao, preferences)

    @Provides
    @Singleton
    fun provideRepoInterface(repo: OneClickRepo): RepoInterface = RepoInterface(repo)


}