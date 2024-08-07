package com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemBannerCustomerBinding
import com.app.gentlemanspa.network.ApiConstants.BASE_FILE
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.BannerItem
import com.bumptech.glide.Glide

class BannerCustomerAdapter(private var bannerCustomerList: ArrayList<BannerItem>) : RecyclerView.Adapter<BannerCustomerAdapter.ViewHolder>() {

    class ViewHolder(val binding : ItemBannerCustomerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBannerCustomerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bannerCustomerList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = bannerCustomerList[position]
        holder.binding.apply {

            Glide.with(holder.itemView.context).load(BASE_FILE+item.bannerImage).placeholder(R.drawable.banner_placeholder).error(R.drawable.banner_placeholder).into(ivProfile)
        }

    }

}