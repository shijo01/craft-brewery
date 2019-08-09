package com.shijo.beercraft.utils

import android.content.Context
import com.shijo.beercraft.BuildConfig
import kotlin.random.Random

class Drawable {
    val drawableArray: Array<Drawable> = arrayOf()

    companion object {


        fun getRandomDrawable(context: Context, rId: Int): Int {
            return context.resources.getIdentifier("r$rId", "drawable", BuildConfig.APPLICATION_ID)

        }
    }
}