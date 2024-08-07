package com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemLocationBinding
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.LocationItem

class LocationAddressAdapter(private var locationAddressList: ArrayList<LocationItem>) : RecyclerView.Adapter<LocationAddressAdapter.ViewHolder>() {

    class ViewHolder(val binding : ItemLocationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLocationBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return locationAddressList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = locationAddressList[position]
        holder.binding.apply {
            tvSpaName.text = item.name
            tvAddressValue.text = "${item.address1 }\n${item.landmark}\n${item.phoneNumber1}"

        }
    }
}