package com.shijo.beercraft.di

import com.shijo.beercraft.di.main.MainFragmentBuildersModule
import com.shijo.beercraft.di.main.MainModule
import com.shijo.beercraft.di.main.MainScope
import com.shijo.beercraft.di.main.MainViewModelsModule
import com.shijo.beercraft.ui.main.BeerCraftActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @MainScope
    @ContributesAndroidInjector(
        modules = [MainFragmentBuildersModule::class, MainViewModelsModule::class, MainModule::class]
    )
    abstract fun contributeMainActivity(): BeerCraftActivity
}