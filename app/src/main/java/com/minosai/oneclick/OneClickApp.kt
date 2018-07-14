package com.minosai.oneclick

import android.app.Activity
import android.app.Application
import com.minosai.oneclick.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class OneClickApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
//        initDagger()
        AppInjector.init(this)
    }

}