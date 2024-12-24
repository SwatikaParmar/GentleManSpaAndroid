package com.app.gentlemanspa.ui.customerDashboard.fragment.orderDetail.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemOrderDetailsBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.orderDetail.model.OrderDetailsProductItem

import com.bumptech.glide.Glide

class OrderDetailsAdapter (private var productList: List<OrderDetailsProductItem>) :
    RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemOrderDetailsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemOrderDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = productList[position]
        holder.binding.apply {
            tvProductName.text = item.productName
            tvProductPrice.text = "$${item.price}"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE + item.productImage)
                .placeholder(
                    R.drawable.no_product
                ).error(R.drawable.no_product).into(ivProduct)
            root.setOnClickListener {
            }

        }

    }



}