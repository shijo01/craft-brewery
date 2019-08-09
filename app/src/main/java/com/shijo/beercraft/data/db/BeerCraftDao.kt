package com.shijo.beercraft.data.db

import androidx.room.*
import com.shijo.beercraft.data.model.Beer
import io.reactivex.Flowable


@Dao
interface BeerCraftDao {

    @get:Query("SELECT * FROM beer")
    val all: List<Beer>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg beer: Beer)

    @Query("DELETE FROM beer")
    fun nukeTable()

    @Query("SELECT * FROM beer WHERE name LIKE :name")
    fun searchBeer(name: String): Flowable<List<Beer>>

    @Query("SELECT * FROM beer WHERE id == :id")
    fun getBeerById(id: Int): Beer

    @Update
    fun updateBeer(beer: Beer): Int

    @Query("SELECT * FROM beer ORDER BY abv ASC")
    fun getSortedAscList(): Flowable<List<Beer>>

    @Query("SELECT * FROM beer ORDER BY abv DESC")
    fun getSortedDescList(): Flowable<List<Beer>>

}