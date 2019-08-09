package com.shijo.beercraft.ui.main.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : ViewModel() {
    companion object {
        private val TAG = ProfileViewModel::class.java.simpleName
    }

    init {
        Log.d(TAG, "ProfileViewModel: view model created")

    }


}