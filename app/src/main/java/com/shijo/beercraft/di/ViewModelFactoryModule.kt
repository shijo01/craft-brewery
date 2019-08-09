package com.shijo.beercraft.di

import androidx.lifecycle.ViewModelProvider
import com.shijo.beercraft.utils.viewmodel.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
public abstract class ViewModelFactoryModule {
    @Binds
    public abstract fun bindViewModelFactory(
        viewModelProviderFactory: ViewModelProviderFactory
    ): ViewModelProvider.Factory
}