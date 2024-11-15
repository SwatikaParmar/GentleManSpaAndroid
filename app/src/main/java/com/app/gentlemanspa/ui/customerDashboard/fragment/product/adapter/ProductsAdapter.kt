package com.app.gentlemanspa.ui.customerDashboard.fragment.product.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
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

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var countInCart :Int =0
        val item = productsList[position]
        holder.binding.apply {
            tvServiceName.text = item.name
            tvRupees.text = "$${String.format("%.2f",item.listingPrice!!.toDouble())}"
            tvLessRupees.text = "$${String.format("%.2f",item.basePrice!!.toDouble())}"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.image).error(R.drawable.no_product).placeholder(
                R.drawable.no_product).into(ivService)
            Log.d("countInCart","countInCart->${item.countInCart}")
            countInCart=item.countInCart
            tvCount.text = countInCart.toString()

            if (item.countInCart>0){
                clAddCart.setGone()
                clPlusMinus.setVisible()
            }else{
                clAddCart.setVisible()
                clPlusMinus.setGone()
            }
            clAddCart.setOnClickListener {
                countInCart=1
                productsCallbacks.addOrUpdateProductInCart(item.productId,countInCart,item.stock)
            }

            ivPlus.setOnClickListener {
                countInCart++
                productsCallbacks.addOrUpdateProductInCart(item.productId,countInCart,item.stock)
            }

            ivMinus.setOnClickListener {
                if (countInCart>=0){
                    countInCart--
                    productsCallbacks.addOrUpdateProductInCart(item.productId,countInCart,item.stock)
                }
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
        fun addOrUpdateProductInCart(productId:Int,countInCard:Int,stock:Int)

    }

}