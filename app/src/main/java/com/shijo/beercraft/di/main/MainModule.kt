package com.shijo.beercraft.di.main

import com.shijo.beercraft.data.network.BeerApi
import com.shijo.beercraft.ui.main.beercraft.BeerListAdapter
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {
  @Module
  companion object {
    @MainScope
    @Provides
    @JvmStatic
    fun provideMainApi(retrofit: Retrofit): BeerApi {
      return retrofit.create(BeerApi::class.java)
    }

    @MainScope
    @Provides
    @JvmStatic
    fun provideBeerListAdapter(): BeerListAdapter {
      return BeerListAdapter()
    }
  }
}