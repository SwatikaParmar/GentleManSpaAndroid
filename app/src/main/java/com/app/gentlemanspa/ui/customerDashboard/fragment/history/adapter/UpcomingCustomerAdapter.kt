package com.app.gentlemanspa.ui.customerDashboard.fragment.history.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemUpcomingCustomerBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentItem
import com.app.gentlemanspa.utils.formatDayDate
import com.bumptech.glide.Glide

class UpcomingCustomerAdapter(private val dataList:ArrayList<UpcomingServiceAppointmentItem>) : RecyclerView.Adapter<UpcomingCustomerAdapter.ViewHolder>()  {
    private lateinit var upcomingCallbacks: UpcomingCallbacks

    class ViewHolder(val binding : ItemUpcomingCustomerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUpcomingCustomerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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
            Log.d("professionalName","name:${item.professionalName}")
            tvBookingId.text="BOOKING ID: ${item.orderId}"
            tvDate.text="${formatDayDate(item.slotDate)} at ${item.fromTime}"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.image).error(
                R.drawable.no_product).into(ivProfessional)

            btnCancel.setOnClickListener{
                upcomingCallbacks.upcomingCancel(item)
            }
            btnReschedule.setOnClickListener{
                upcomingCallbacks.upcomingReschedule(item)
            }
            root.setOnClickListener {
            }
        }
    }

    fun setOnUpcomingCallbacks(onClick : UpcomingCallbacks) {
        upcomingCallbacks =onClick
    }
    interface UpcomingCallbacks {
        fun upcomingCancel(item: UpcomingServiceAppointmentItem)
        fun upcomingReschedule(item: UpcomingServiceAppointmentItem)
    }
}