package com.shijo.beercraft.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class Cart(@field:PrimaryKey val id: Int, var qty: Int)