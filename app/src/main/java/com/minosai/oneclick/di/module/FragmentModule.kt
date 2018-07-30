package com.minosai.oneclick.di.module

import com.minosai.oneclick.ui.fragment.credentials.CredentialsFragment
import com.minosai.oneclick.ui.fragment.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun contributeCredentialsFragment(): CredentialsFragment

}