package com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemProductCategoriesBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductCategoriesItem
import com.bumptech.glide.Glide

class ProductCategoriesAdapter(private var productCategoriesList: ArrayList<ProductCategoriesItem>) : RecyclerView.Adapter<ProductCategoriesAdapter.ViewHolder>() {

    private lateinit var productCategoriesCallbacks: ProductCategoriesCallbacks
    class ViewHolder(val binding : ItemProductCategoriesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductCategoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productCategoriesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = productCategoriesList[position]
        holder.binding.apply {
            tvCategories.text = item.categoryName
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.categoryImage).placeholder(
                R.drawable.service_placeholder).error(R.drawable.service_placeholder).into(ivCategories)

            root.setOnClickListener {
                productCategoriesCallbacks.rootProductCategories(item,position)
            }
        }

    }

    fun setOnProductCategoriesCallbacks(onClick : ProductCategoriesCallbacks) {
        productCategoriesCallbacks =onClick
    }

    interface ProductCategoriesCallbacks {
        fun rootProductCategories(item: ProductCategoriesItem, position: Int)
    }

}