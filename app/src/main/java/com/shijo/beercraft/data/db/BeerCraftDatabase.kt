package com.shijo.beercraft.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shijo.beercraft.data.model.Beer
import com.shijo.beercraft.data.model.Cart

@Database(entities = [Beer::class, Cart::class], version = 1, exportSchema = false)
abstract class BeerCraftDatabase : RoomDatabase() {
    abstract fun beerCraftDao(): BeerCraftDao
    abstract fun cartDao(): CartDao
}