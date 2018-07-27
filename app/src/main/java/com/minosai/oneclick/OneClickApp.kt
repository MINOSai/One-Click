package com.minosai.oneclick

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.BroadcastReceiver
import com.minosai.oneclick.di.AppInjector
import com.minosai.oneclick.util.helper.LoginLogoutBroadcastHelper
import com.minosai.oneclick.util.service.WebService
import dagger.android.*
import javax.inject.Inject

class OneClickApp : Application(), HasActivityInjector, HasBroadcastReceiverInjector, HasServiceInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var broadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>
    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>

    override fun activityInjector() = dispatchingAndroidInjector

    override fun broadcastReceiverInjector() = broadcastReceiverInjector

    override fun serviceInjector() = serviceInjector

    override fun onCreate() {
        super.onCreate()
//        initDagger()
        AppInjector.init(this)

        LoginLogoutBroadcastHelper.sendLoginLogoutBroadcast(this, WebService.Companion.RequestType.LOGOUT)
    }

}