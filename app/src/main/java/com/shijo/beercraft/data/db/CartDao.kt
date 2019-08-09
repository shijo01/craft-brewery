package com.shijo.beercraft.data.db

import androidx.room.*
import com.shijo.beercraft.data.model.Cart

@Dao
interface CartDao {
    @get:Query("SELECT * FROM cart")
    val all: List<Cart>

    @Insert
    fun insert(vararg cart: Cart)

    @Query("SELECT * FROM cart WHERE id == :id")
    abstract fun getCartById(id: Int): Cart

    @Update
    abstract fun updateCart(cart: Cart): Int

    @Delete
    abstract fun delete(cart: Cart)
}