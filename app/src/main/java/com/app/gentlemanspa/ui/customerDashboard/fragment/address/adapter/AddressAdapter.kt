package com.app.gentlemanspa.ui.customerDashboard.fragment.address.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemAddressBinding
import com.app.gentlemanspa.databinding.ItemCartProductsBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.AddressItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.adapter.CartProductsAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.Product
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.bumptech.glide.Glide

class AddressAdapter (var productsList: List<AddressItem>) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    private lateinit var addressCallbacks: AddressCallbacks
    class ViewHolder(val binding : ItemAddressBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = productsList[position]
        holder.binding.apply {
            tvAddressType.text = item.addressType
            val fullAddress= "${item.houseNoOrBuildingName},${item.streetAddresss},${item.nearbyLandMark},${item.city},${item.state}"
            var cleanFullAddress=fullAddress.replace(",,",",")
            if (cleanFullAddress.endsWith(",")){
                cleanFullAddress = cleanFullAddress.dropLast(1)
            }
            tvAddressName.text = cleanFullAddress
            if (item.status){
                tvStatus.setVisible()
                tvStatus.text="Primary"
            }else{
                tvStatus.setGone()
            }


            ivAddressOptions.setOnClickListener {
                addressCallbacks.addressOption(item)

            }
            cvItem.setOnClickListener {
                addressCallbacks.rootAddress(item.customerAddressId)
            }

            rlDelete.setOnClickListener {
                addressCallbacks.deleteAddress(item.customerAddressId)
            }
        }

    }

    fun setOnClickAddress(onClick : AddressCallbacks){
        addressCallbacks = onClick
    }

    interface AddressCallbacks{
        fun rootAddress(customerAddressId:Int)
        fun addressOption(addressItem: AddressItem)
        fun deleteAddress(customerAddressId:Int)

    }

}