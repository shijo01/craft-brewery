package com.shijo.beercraft.base

import com.google.android.material.navigation.NavigationView
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        private val TAG = BaseActivity::class.java.simpleName
    }
}