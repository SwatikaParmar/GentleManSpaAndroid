package com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemProductsBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsListItem
import com.bumptech.glide.Glide

class ProductsAdapter(var productsList: ArrayList<ProductsListItem>) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private lateinit var productsCallbacks: ProductsCallbacks

    class ViewHolder(val binding : ItemProductsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = productsList[position]
        holder.binding.apply {
            tvProducts.text = item.name
            tvRupees.text = "$${item.listingPrice}"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.image).into(ivCategories)
            root.setOnClickListener {
                productsCallbacks.rootProducts(item)
            }
        }

    }

    fun setOnClickProducts(onClick : ProductsCallbacks){
        productsCallbacks = onClick
    }

    interface ProductsCallbacks{
        fun rootProducts(item: ProductsListItem)
    }

}