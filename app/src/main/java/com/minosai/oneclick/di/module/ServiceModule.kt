package com.minosai.oneclick.di.module

import com.minosai.oneclick.util.service.OneClickTileService
import com.minosai.oneclick.util.service.WifiService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract fun contributesTileService(): OneClickTileService

    @ContributesAndroidInjector
    abstract fun contributesWifiService(): WifiService

}