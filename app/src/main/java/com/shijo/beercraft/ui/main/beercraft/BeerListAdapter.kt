package com.shijo.beercraft.ui.main.beercraft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shijo.beercraft.data.model.Beer
import com.shijo.beercraft.databinding.BeerListItemBinding
import com.shijo.beercraft.utils.Drawable
import java.util.*

class BeerListAdapter : RecyclerView.Adapter<BeerListAdapter.BeerViewHolder>() {

    private var beers: List<Beer> = ArrayList()
    private lateinit var cartClickListener: CartClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val binding = BeerListItemBinding.inflate(LayoutInflater.from(parent.context))
        return BeerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        holder.bind(beers[position])
    }

    override fun getItemCount(): Int {
        return beers.size
    }

    fun setBeerList(beers: List<Beer>?) {
        if (beers != null) {
            this.beers = beers
            notifyDataSetChanged()
        }
    }

    fun setCartClickListener(cartClickListener: CartClickListener) {
        this.cartClickListener = cartClickListener
    }

    inner class BeerViewHolder(private val binding: BeerListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(beer: Beer) {
            binding.beer = beer
            Glide.with(binding.root.context).load(Drawable.getRandomDrawable(binding.root.context, beer.id % 7 + 1))
                .into(binding.imageViewIcon)
            binding.textViewQty.visibility = if (beer.cartQty > 0) View.VISIBLE else View.GONE
            binding.imageViewMinus.visibility = if (beer.cartQty > 0) View.VISIBLE else View.GONE
            binding.imageViewPlus.setOnClickListener { cartClickListener.onAddClicked(beer) }
            binding.imageViewMinus.setOnClickListener { cartClickListener.onRemoveClicked(beer) }
            binding.executePendingBindings()
        }
    }
}

interface CartClickListener {
    fun onAddClicked(beer: Beer)
    fun onRemoveClicked(beer: Beer)
}