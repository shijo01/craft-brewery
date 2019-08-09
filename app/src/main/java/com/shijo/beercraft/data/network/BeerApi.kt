package com.shijo.beercraft.data.network

import com.shijo.beercraft.data.model.Beer
import io.reactivex.Observable
import retrofit2.http.GET

interface BeerApi {
    @GET("beercraft")
    fun getBeers(): Observable<List<Beer>>
}