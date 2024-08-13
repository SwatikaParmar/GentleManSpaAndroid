package com.app.gentlemanspa.ui.customerDashboard.fragment.service.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.context
import com.app.gentlemanspa.databinding.ItemCategoriesServiceBinding
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.CategoriesItem

class ServiceCategoriesAdapter(
    private var categoriesList: ArrayList<CategoriesItem>,
    private var positionHor: Int
) : RecyclerView.Adapter<ServiceCategoriesAdapter.ViewHolder>() {

    private lateinit var serviceCategoriesCallbacks :ServiceCategoriesCallbacks

    class ViewHolder(val binding : ItemCategoriesServiceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoriesServiceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = categoriesList[position]
        holder.binding.apply {
            tvServiceName.text = item.categoryName

            if (position ==positionHor){
                positionHor =-1
                clFirst.isSelected =true
                tvServiceName.setTextColor(ContextCompat.getColor(context, R.color.black))
            }else{
                clFirst.isSelected =false
                tvServiceName.setTextColor(ContextCompat.getColor(context, R.color.white))

            }

            root.setOnClickListener {
                positionHor = position
                notifyDataSetChanged()
                serviceCategoriesCallbacks.rootServiceCategories(item,position)
            }
        }

    }

    fun setOnServiceCategories(onClick: ServiceCategoriesCallbacks) {
        serviceCategoriesCallbacks =onClick
    }


    interface ServiceCategoriesCallbacks{
        fun rootServiceCategories(item: CategoriesItem?, position: Int?)
    }

}