package com.minosai.oneclick.di.module

import com.minosai.oneclick.util.receiver.AlarmReceiver
import com.minosai.oneclick.util.receiver.WifiReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ReceiverModule {

    @ContributesAndroidInjector
    abstract fun contributeWifiReceiver(): WifiReceiver

    @ContributesAndroidInjector
    abstract fun contributeAlarmReceiver(): AlarmReceiver
}