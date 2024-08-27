package com.app.gentlemanspa.ui.professionalDashboard.fragment.product.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemProductProfessionalBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsListItem
import com.bumptech.glide.Glide
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter

class ProductProfessionalAdapter(private var productsList: ArrayList<ProductsListItem>) : RecyclerSwipeAdapter<ProductProfessionalAdapter.ViewHolder>() {

    private lateinit var productProfessionalCallbacks: ProductProfessionalCallbacks

    class ViewHolder(val binding : ItemProductProfessionalBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductProfessionalBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var count :Int =1
        mItemManger.bindView(holder.itemView, position)
        val item = productsList[position]
        holder.binding.apply {

            if (position === productsList.size - 1) {
                val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
                params.bottomMargin = 200
                holder.itemView.layoutParams = params
            } else {
                val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
                params.bottomMargin = 0
                holder.itemView.layoutParams = params
            }
            tvServiceName.text = item.name
            tvDescription.text = item.description
            tvRupees.text = "$${item.listingPrice}"
            tvInStock.text = "In Stock:${item.stock}"
            tvLessRupees.text = "$${item.basePrice}"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.image).error(R.drawable.no_product).placeholder(R.drawable.no_product).into(ivService)


            cvProduct.setOnClickListener {
                productProfessionalCallbacks.rootProductProfessional(item)
            }

            rowBG.setOnClickListener {
                productProfessionalCallbacks.deleteProductItem(item,position)
            }

            ivUpdateProduct.setOnClickListener {
                productProfessionalCallbacks.updateProductItem(item)
            }


        }

    }

    fun setOnClickProductProfessional(onClick : ProductProfessionalCallbacks){
        productProfessionalCallbacks = onClick
    }

    interface ProductProfessionalCallbacks{
        fun rootProductProfessional(item: ProductsListItem)
        fun deleteProductItem(item: ProductsListItem, position: Int)
        fun updateProductItem(item: ProductsListItem)
    }

}