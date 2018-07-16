package com.minosai.oneclick.di

import android.app.Application
import com.minosai.oneclick.OneClickApp
import com.minosai.oneclick.di.module.ActivityModule
import com.minosai.oneclick.di.module.AppModule
import com.minosai.oneclick.di.module.ReceiverModule
import com.minosai.oneclick.di.module.ServiceModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityModule::class,
    ReceiverModule::class,
    ServiceModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application) : Builder
        fun build() : AppComponent
    }

    fun inject(oneClickApp: OneClickApp)
}