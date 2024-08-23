package com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemProductCategoryBottomBinding
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductCategoriesItem


class ProductCategoryAdapter(private val productCategoriesList: ArrayList<ProductCategoriesItem>) : RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder>(){

    private lateinit var productCategoryCallback: ProductCategoryCallback
    class ViewHolder(val binding : ItemProductCategoryBottomBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productCategoriesList[position]
        holder.binding.apply {
            tvTitle.text = item.categoryName
            root.setOnClickListener {
                productCategoryCallback.rootProductCategory(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductCategoryBottomBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return productCategoriesList.size
    }


    fun setOnClickUploadProduct(click : ProductCategoryCallback){
        productCategoryCallback = click
    }



    interface ProductCategoryCallback {
        fun rootProductCategory(item: ProductCategoriesItem)
    }

}