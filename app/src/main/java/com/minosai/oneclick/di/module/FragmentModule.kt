package com.minosai.oneclick.di.module

import com.minosai.oneclick.ui.credentials.CredentialsFragment
import com.minosai.oneclick.ui.main.MainFragment
import com.minosai.oneclick.ui.preferences.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun contributeCredentialsFragment(): CredentialsFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

}