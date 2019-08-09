package com.shijo.beercraft.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.shijo.beercraft.R
import com.shijo.beercraft.base.BaseActivity
import com.shijo.beercraft.ui.main.beercraft.BeerListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class BeerCraftActivity : BaseActivity() {

    @Inject
    lateinit var beerListViewModel: BeerListViewModel
    private lateinit var menu: Menu

    companion object {
        private val TAG = BeerCraftActivity::class.java.simpleName
    }

    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null)
                beerListViewModel.searchBeerList("%$query%")
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText != null)
                beerListViewModel.searchBeerList("%$newText%")
            return true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNavigation()

        /**
         * Observing for sort type live data. If sorted type is ASC we set the icon of DESC and vice versa
         */
        beerListViewModel.getSortTypeObserver().observe(this, Observer { sortType ->
            if (!::menu.isInitialized) {
                return@Observer
            }
            if (sortType) {
                menu.getItem(1).icon = ContextCompat.getDrawable(this, R.drawable.ic_sort_d)
            } else {
                menu.getItem(1).icon = ContextCompat.getDrawable(this, R.drawable.ic_sort_a)
            }
        })
    }

    /**
     * Initializes the navigation components
     */
    private fun initNavigation() {
        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        NavigationUI.setupWithNavController(nav_view, navController)
        nav_view.setNavigationItemSelectedListener(this)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        this.menu = menu
        val searchView = menu.findItem(R.id.action_search)
            .actionView as SearchView

        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(onQueryTextListener)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                return if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    true
                } else {
                    false
                }
            }

            R.id.action_sort -> {
                beerListViewModel.sort()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {
            R.id.profile -> {
                if (isValidDestination(R.id.profileFragment))
                    Navigation.findNavController(
                        this,
                        R.id.navHostFragment
                    ).navigate(R.id.profileFragment)
            }
            R.id.beercraft -> {
                if (isValidDestination(R.id.beerListFragment)) {
                    val navOptions: NavOptions =
                        NavOptions.Builder().setPopUpTo(R.id.main, true).build()
                    Navigation.findNavController(this, R.id.navHostFragment).navigate(
                        R.id.beerListFragment,
                        null, navOptions
                    )
                }
            }
        }
        menuItem.isChecked = true
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * Checks whether the user selects already selected option or not.
     */
    private fun isValidDestination(destinationId: Int): Boolean {
        return destinationId != Navigation.findNavController(
            this,
            R.id.navHostFragment
        ).currentDestination?.id
    }

    /**
     * Setting up the back arrow to the navigation controller
     */
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.navHostFragment),
            drawer_layout
        )
    }

}