package com.app.gentlemanspa.ui.customerDashboard.fragment.cart.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemCartProductsBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.Product
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.bumptech.glide.Glide

class CartProductsAdapter (var productsList: List<Product>) : RecyclerView.Adapter<CartProductsAdapter.ViewHolder>() {

    private lateinit var cartProductsCallbacks: CartProductsCallbacks
    class ViewHolder(val binding : ItemCartProductsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartProductsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var countInCart :Int =0

        val item = productsList[position]
        holder.binding.apply {
            tvServiceName.text = item.name
            tvRupees.text = "$${item.listingPrice}"
            tvLessRupees.text = "$${item.basePrice}"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.productImage).error(R.drawable.no_product).placeholder(
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
                cartProductsCallbacks.addOrUpdateProductInCart(item.productId,countInCart,item.stock)

            }

            ivPlus.setOnClickListener {
                countInCart++
                cartProductsCallbacks.addOrUpdateProductInCart(item.productId,countInCart,item.stock)

            }

            ivMinus.setOnClickListener {
                if (countInCart>=0){
                    countInCart--
                    cartProductsCallbacks.addOrUpdateProductInCart(item.productId,countInCart,item.stock)
                }
            }

            root.setOnClickListener {
                cartProductsCallbacks.rootProducts(item)
            }


        }

    }

    fun setOnClickCartProducts(onClick : CartProductsCallbacks){
        cartProductsCallbacks = onClick
    }

    interface CartProductsCallbacks{
        fun rootProducts(item: Product)
        fun addOrUpdateProductInCart(productId:Int,countInCard:Int,stock:Int)

    }

}