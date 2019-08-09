package com.shijo.beercraft.data.model


import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "beer")
data class Beer(
    @Json(name = "abv")
    val abv: String,
    @Json(name = "ibu")
    val ibu: String,
    @field:PrimaryKey
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "ounces")
    val ounces: Double,
    @Json(name = "style")
    val style: String,
    var cartQty: Int = 0
) {
    @Ignore
    constructor() : this("", "", -1, "", 0.0, "")
}