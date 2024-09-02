package com.app.gentlemanspa.ui.professionalDashboard.fragment.productDetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemBannerCustomerBinding
import com.app.gentlemanspa.network.ApiConstants.BASE_FILE
import com.bumptech.glide.Glide

class ProductDetailBannerAdapter(private var productBannerList: ArrayList<String>) : RecyclerView.Adapter<ProductDetailBannerAdapter.ViewHolder>() {

    class ViewHolder(val binding : ItemBannerCustomerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBannerCustomerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productBannerList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = productBannerList[position]
        holder.binding.apply {

            Glide.with(holder.itemView.context).load(BASE_FILE +item).placeholder(R.drawable.banner_placeholder).error(
                R.drawable.banner_placeholder).into(ivProfile)
        }

    }

}