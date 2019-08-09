package com.shijo.beercraft.di.main

import androidx.lifecycle.ViewModel
import com.shijo.beercraft.di.ViewModelKey
import com.shijo.beercraft.ui.main.beercraft.BeerListViewModel
import com.shijo.beercraft.ui.main.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {
  @Binds
  @IntoMap
  @ViewModelKey(ProfileViewModel::class)
  abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(BeerListViewModel::class)
  abstract fun bindBeerListViewModel(beerListViewModel: BeerListViewModel): ViewModel
}