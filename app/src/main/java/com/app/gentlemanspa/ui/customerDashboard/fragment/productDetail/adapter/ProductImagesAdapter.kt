package com.app.gentlemanspa.ui.customerDashboard.fragment.productDetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemBannerCustomerBinding
import com.app.gentlemanspa.databinding.ItemProductDetailBinding
import com.app.gentlemanspa.network.ApiConstants.BASE_FILE
import com.bumptech.glide.Glide

class ProductImagesAdapter (private var dataList: ArrayList<String>) : RecyclerView.Adapter<ProductImagesAdapter.ViewHolder>() {

    class ViewHolder(val binding : ItemProductDetailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = dataList[position]
        holder.binding.apply {
            Glide.with(holder.itemView.context).load(BASE_FILE +item).placeholder(R.drawable.no_product).error(
                R.drawable.no_product).into(ivProfile)
        }

    }

}