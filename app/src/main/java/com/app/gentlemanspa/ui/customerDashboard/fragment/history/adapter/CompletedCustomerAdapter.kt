package com.app.gentlemanspa.ui.customerDashboard.fragment.history.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemConfirmedCustomerBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsListItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.adapter.ProductsAdapter.ProductsCallbacks
import com.app.gentlemanspa.utils.formatDayDate
import com.bumptech.glide.Glide

class CompletedCustomerAdapter(private val dataList:ArrayList<UpcomingServiceAppointmentItem>) : RecyclerView.Adapter<CompletedCustomerAdapter.ViewHolder>()  {
    private lateinit var completeCustomerCallbacks: CompleteCustomerCallbacks
    class ViewHolder(val binding : ItemConfirmedCustomerBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConfirmedCustomerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return dataList.size
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=dataList[position]
        holder.binding.apply {
            tvServiceName.text=item.serviceName
            tvRupees.text="$${item.price}"
            tvAssignedValue.text= item.professionalName
            tvBookingId.text="BOOKING ID: ${item.orderId}"
            tvDate.text="${formatDayDate(item.slotDate)} at ${item.fromTime}"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.image).error(
                R.drawable.no_product).into(ivProfessional)
            root.setOnClickListener {
            }
            tvMessage.setOnClickListener{
                completeCustomerCallbacks.onCompleteCustomerMessageClicked(item)
            }
        }
    }
    fun setOnClickCompleteCustomer(onClick : CompleteCustomerCallbacks){
        completeCustomerCallbacks = onClick
    }
    interface CompleteCustomerCallbacks{
        fun onCompleteCustomerMessageClicked(item:UpcomingServiceAppointmentItem)
    }
}