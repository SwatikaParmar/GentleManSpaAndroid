package com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemCategoriesBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.CategoriesItem
import com.bumptech.glide.Glide

class CategoriesAdapter(private var categoriesList: ArrayList<CategoriesItem>) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private lateinit var categoriesCallbacks: CategoriesCallbacks
    class ViewHolder(val binding : ItemCategoriesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = categoriesList[position]
        holder.binding.apply {
            tvCategories.text = item.categoryName
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.categoryImage).placeholder(
                R.drawable.service_placeholder).error(R.drawable.service_placeholder).into(ivCategories)

            root.setOnClickListener {
                categoriesCallbacks.rootCategories()
            }
        }

    }

    fun setOnCategoriesCallbacks(onClick : CategoriesCallbacks) {
        categoriesCallbacks =onClick
    }

    interface CategoriesCallbacks {
        fun rootCategories()
    }

}