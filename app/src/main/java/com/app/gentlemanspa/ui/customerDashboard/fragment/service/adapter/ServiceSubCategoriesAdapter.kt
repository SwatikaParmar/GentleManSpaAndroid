package com.app.gentlemanspa.ui.customerDashboard.fragment.service.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.context
import com.app.gentlemanspa.databinding.ItemCategoriesServiceBinding
import com.app.gentlemanspa.databinding.ItemServiceSubCategoriesBinding
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.SpaSubCategoriesData

class ServiceSubCategoriesAdapter(
    private var subcategoriesList: ArrayList<SpaSubCategoriesData>,
    private var positionHor: Int
) : RecyclerView.Adapter<ServiceSubCategoriesAdapter.ViewHolder>() {

    private lateinit var serviceSubCategoriesCallbacks :ServiceSubCategoriesCallbacks
    class ViewHolder(val binding : ItemServiceSubCategoriesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemServiceSubCategoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return subcategoriesList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = subcategoriesList[position]
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
           /* if (item.isSelected) {
                clFirst.isSelected = true
                tvServiceName.setTextColor(ContextCompat.getColor(context, R.color.black))
            } else {
                clFirst.isSelected = false
                tvServiceName.setTextColor(ContextCompat.getColor(context, R.color.white))
            }*/

            root.setOnClickListener {
                positionHor = position
               /* for (i in subcategoriesList.indices) {
                    subcategoriesList[i].isSelected = i == position  // Set selected for clicked item
                }*/
                notifyDataSetChanged()
                serviceSubCategoriesCallbacks.rootServiceCategories(item,position)
            }
        }

    }

    fun setOnServiceSubCategories(onClick: ServiceSubCategoriesCallbacks) {
        serviceSubCategoriesCallbacks =onClick
    }


    interface ServiceSubCategoriesCallbacks{
        fun rootServiceCategories(item: SpaSubCategoriesData?, position: Int?)
    }

}