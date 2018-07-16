package com.minosai.oneclick.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.minosai.oneclick.util.service.WebService
import com.minosai.oneclick.util.Constants
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
    fun providePreferences(application: Application): SharedPreferences = application.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideWebService(application: Application, preferences: SharedPreferences): WebService = WebService(application, preferences)

}