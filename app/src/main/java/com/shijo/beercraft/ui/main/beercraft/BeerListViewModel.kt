package com.shijo.beercraft.ui.main.beercraft

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shijo.beercraft.R
import com.shijo.beercraft.data.db.BeerCraftDao
import com.shijo.beercraft.data.db.CartDao
import com.shijo.beercraft.data.model.Beer
import com.shijo.beercraft.data.model.Cart
import com.shijo.beercraft.data.network.BeerApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BeerListViewModel @Inject constructor(
    val beerApi: BeerApi,
    val beerCraftDao: BeerCraftDao,
    val cartDao: CartDao,
    val beerListAdapter: BeerListAdapter
) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val progressBarVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadBeerList() }
    private val ascending: MutableLiveData<Boolean> = MutableLiveData()
    private var loadFromApi = true

    companion object {
        private val TAG = BeerListViewModel::class.java.simpleName
    }

    init {
        //Initial loading of data when user opens the app.
        loadBeerList()
        ascending.value = true
    }

    /**
     * This method searches the keyword in local database and display the results
     */
    fun searchBeerList(query: String) {
        compositeDisposable.add(
            beerCraftDao.searchBeer(query).subscribeOn(Schedulers.computation()).observeOn(
                AndroidSchedulers.mainThread()
            )
                .subscribe({
                    beerListAdapter.setBeerList(it)

                }, {
                    Log.d(TAG, "searchBeerList: $it")
                })
        )
    }

    /**
     * This function load the beer list. There are two scenarios
     * 1. User closes the app and open again or open for first time -> Load data from API clear the database if
     * data exists and load the new fetched data to the local database
     * 2. Method gets called by configuration changes or screen rotation -> Load the data from DB.
     *
     * Once the data is fetched newly the beer list is synced with cart and save it in the databse
     */
    private fun loadBeerList() {
        compositeDisposable.add(Observable.fromCallable { beerCraftDao.all }
            .concatMap { dbBeerList: List<Beer> ->
                if (dbBeerList.isNullOrEmpty() || loadFromApi)
                    beerApi.getBeers().concatMap { apiBeerList ->
                        loadFromApi = false
                        beerCraftDao.nukeTable()
                        beerCraftDao.insertAll(*apiBeerList.toTypedArray())
                        Observable.just(apiBeerList)
                    }
                else
                    Observable.just(dbBeerList)
            }.concatMap { beerList: List<Beer> ->
                val cartList = cartDao.all
                if (cartList.isNullOrEmpty()) {
                    Observable.just(beerList)
                } else {
                    syncWithCart(beerList, cartList)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveBeerListStart() }
            .doOnTerminate { onRetrieveBeerListFinish() }
            .subscribe({ beerList -> onRetrieveBeerListSuccess(beerList) }, { onRetrieveBeerListError(it) })
        )
    }

    /**
     * This function sync beer list with existing cart.
     */
    private fun syncWithCart(beerList: List<Beer>, cartList: List<Cart>): Observable<List<Beer>> {
        beerList.forEach { beer ->
            if (cartList.any { cart ->
                    cart.id == beer.id
                }) {
                val cartProdList = cartList.filter { it.id == beer.id }
                if (!cartProdList.isNullOrEmpty()) {
                    beer.cartQty = cartProdList[0].qty
                    beerCraftDao.updateBeer(beer)
                }
            }
        }
        return Observable.just(beerList)
    }

    private fun onRetrieveBeerListSuccess(beerList: List<Beer>) {
        Log.d(TAG, "onRetrieveBeerListSuccess: ${beerList.size}")
        beerListAdapter.setBeerList(beerList)
    }

    private fun onRetrieveBeerListStart() {
        progressBarVisibility.value = View.VISIBLE
        errorMessage.value = null

    }

    private fun onRetrieveBeerListFinish() {
        progressBarVisibility.value = View.GONE

    }

    private fun onRetrieveBeerListError(throwable: Throwable) {
        errorMessage.value = R.string.loading_error
        Log.e(TAG, "onRetrieveBeerListError: $throwable")


    }


    fun getProgressBarVisibility(): MutableLiveData<Int> {
        return progressBarVisibility
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    /**
     * This function will add a product to cart. It will update the local cart and beer list.
     */
    fun addToCart(beer: Beer) {
        progressBarVisibility.value = View.VISIBLE
        compositeDisposable.add(
            Observable
                .fromCallable { beerCraftDao.getBeerById(beer.id) }
                .subscribeOn(Schedulers.computation())
                .concatMap { dbBeer: Beer ->
                    dbBeer.cartQty = beer.cartQty + 1
                    val cart = cartDao.getCartById(beer.id)
                    if (cart == null) {
                        cartDao.insert(Cart(dbBeer.id, beer.cartQty + 1))
                    } else {
                        cart.qty = beer.cartQty + 1
                        cartDao.updateCart(cart)
                    }

                    Log.d(TAG, "addToCart: $cart")


                    Observable.fromCallable { beerCraftDao.updateBeer(dbBeer) }
                }.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it == 1) {
                        beer.cartQty += 1
                        beerListAdapter.notifyDataSetChanged()
                        progressBarVisibility.value = View.GONE
                    }
                }
        )
    }

    /**
     * This function will remove the quantity by one from cart. If cart qty becomes 0 it will
     * remove the beer from cart. It will update the local cart and beer list.
     */
    fun removeFromCart(beer: Beer) {
        progressBarVisibility.value = View.VISIBLE
        if (beer.cartQty <= 0) {
            beerListAdapter.notifyDataSetChanged()
        }
        compositeDisposable.add(
            Observable
                .fromCallable { beerCraftDao.getBeerById(beer.id) }
                .subscribeOn(Schedulers.computation())
                .concatMap { dbBeer: Beer ->
                    if (dbBeer.cartQty <= 1) {
                        dbBeer.cartQty = 0
                    } else
                        dbBeer.cartQty -= 1

                    val cart = cartDao.getCartById(beer.id)
                    if (cart != null) {
                        cart.qty -= 1
                        if (cart.qty <= 0)
                            cartDao.delete(cart)
                        else
                            cartDao.updateCart(cart)
                    }
                    Log.d(TAG, "removeFromCart: $cart")


                    Observable.fromCallable { beerCraftDao.updateBeer(dbBeer) }
                }.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it == 1) {
                        beer.cartQty -= 1
                        beerListAdapter.notifyDataSetChanged()
                        progressBarVisibility.value = View.GONE
                    }
                }
        )

    }

    /**
     * This function will sort the beer based on alcohol content asc and display
     */

    private fun sortAscending() {
        compositeDisposable.add(
            beerCraftDao.getSortedAscList().subscribeOn(Schedulers.computation()).observeOn(
                AndroidSchedulers.mainThread()
            )
                .subscribe({
                    beerListAdapter.setBeerList(it)

                }, {
                    Log.e(TAG, "sortAscending: $it")
                })
        )

    }

    /**
     * This function will sort the beer based on alcohol content desc and display
     */

    private fun sortDescending() {


        compositeDisposable.add(
            beerCraftDao.getSortedDescList().subscribeOn(Schedulers.computation()).observeOn(
                AndroidSchedulers.mainThread()
            )
                .subscribe({
                    beerListAdapter.setBeerList(it)

                }, {

                })
        )

    }

    fun sort() {
        if (ascending.value!!) {
            sortAscending()
            ascending.value = false
        } else {
            sortDescending()
            ascending.value = true
        }
    }

    fun getSortTypeObserver(): LiveData<Boolean> {
        return ascending
    }
}