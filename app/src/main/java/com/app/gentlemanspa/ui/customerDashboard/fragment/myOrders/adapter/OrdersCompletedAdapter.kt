package com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemOrdersProcessingBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.model.MyOrdersDataItem
import com.app.gentlemanspa.utils.convertDateFormat
import com.bumptech.glide.Glide

class OrdersCompletedAdapter (private val dataList: ArrayList<MyOrdersDataItem>) :
    RecyclerView.Adapter<OrdersCompletedAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemOrdersProcessingBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemOrdersProcessingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]

        holder.binding.apply {
            tvServiceName.text = item.productName
            tvPrice.text = "$${item.price}"
            // tvAssignedValue.text= item.professionalName
            Log.d("professionalName", "name:${item.professionalName}")
            tvBookingId.text = "BOOKING ID: ${item.orderId}"
            tvDate.text = "ORDER DATE: ${convertDateFormat(item.orderDate)}"
            tvOrderStatus.text = item.orderStatus
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE + item.productImage)
                .error(
                    R.drawable.no_product
                ).into(ivProfessional)


            root.setOnClickListener {
            }
        }
    }

}