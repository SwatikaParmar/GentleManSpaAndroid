package com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemProductsBinding

class ProductsAdapter : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    class ViewHolder(val binding : ItemProductsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

       /* val item = categoriesList[position]
        holder.binding.apply {
            tvCategories.text = item.categoryName
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.categoryImage).into(ivCategories)
        }*/

    }

}