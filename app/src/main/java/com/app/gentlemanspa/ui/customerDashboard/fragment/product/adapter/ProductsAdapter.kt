package com.app.gentlemanspa.ui.customerDashboard.fragment.product.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemProductsBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsListItem
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
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
        var count :Int =1

        val item = productsList[position]
        holder.binding.apply {
            tvServiceName.text = item.name
            tvRupees.text = "$${item.listingPrice}"
            tvLessRupees.text = "$${item.basePrice}"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.image).into(ivService)

            ivMinus.setOnClickListener {

                count --
                if (count>=1){
                    tvCount.text = count.toString()
                }else{
                    clAddCart.setVisible()
                    clPlusMinus.setGone()

                }

            }

            clAddCart.setOnClickListener {
                count =1
                clAddCart.setGone()
                clPlusMinus.setVisible()
                tvCount.text =count.toString()

            }

            ivPlus.setOnClickListener {
                count ++
                tvCount.text = count.toString()
            }
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