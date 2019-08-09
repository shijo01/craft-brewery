package com.shijo.beercraft.ui.main.beercraft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shijo.beercraft.R
import com.shijo.beercraft.data.model.Beer
import com.shijo.beercraft.databinding.BeerListBinding
import com.shijo.beercraft.utils.viewmodel.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BeerListFragment : DaggerFragment() {
    @Inject
    lateinit var beerListAdapter: BeerListAdapter
    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    private lateinit var viewModel: BeerListViewModel
    lateinit var binding: BeerListBinding
    private var errorSnackbar: Snackbar? = null


    companion object {
        private val TAG = BeerListFragment::class.java.simpleName
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.beer_list_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(
            BeerListViewModel::class.java
        )
        beerListAdapter.setCartClickListener(object : CartClickListener {
            override fun onAddClicked(beer: Beer) {
                viewModel.addToCart(beer)

            }

            override fun onRemoveClicked(beer: Beer) {
                viewModel.removeFromCart(beer)
            }

        })
        binding.viewModel = viewModel
        binding.recyclerViewBeerList.layoutManager = LinearLayoutManager(activity)
        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.errorMessage.removeObservers(viewLifecycleOwner)
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->

            if (null != errorMessage) {
                showError(errorMessage)
            } else {
                hideError()
            }

        })
    }

    private fun hideError() {
        errorSnackbar?.dismiss()

    }

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

}