package com.minosai.oneclick.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.minosai.oneclick.network.WebService
import com.minosai.oneclick.util.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideWebService(): WebService = Retrofit.Builder()
            .baseUrl("http://phc.prontonetworks.com/cgi-bin/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(WebService::class.java)

}