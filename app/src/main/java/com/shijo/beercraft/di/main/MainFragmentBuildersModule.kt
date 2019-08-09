package com.shijo.beercraft.di.main

import com.shijo.beercraft.ui.main.beercraft.BeerListFragment
import com.shijo.beercraft.ui.main.profile.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeBeerListFragment(): BeerListFragment
}