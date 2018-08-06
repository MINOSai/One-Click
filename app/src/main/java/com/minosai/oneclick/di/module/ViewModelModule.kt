package com.minosai.oneclick.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.minosai.oneclick.di.key.ViewModelKey
import com.minosai.oneclick.ui.FactoryViewModel
import com.minosai.oneclick.ui.credentials.CredentialsViewModel
import com.minosai.oneclick.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CredentialsViewModel::class)
    abstract fun bindCredentialsViewModel(credentialsViewModel: CredentialsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: FactoryViewModel): ViewModelProvider.Factory

}