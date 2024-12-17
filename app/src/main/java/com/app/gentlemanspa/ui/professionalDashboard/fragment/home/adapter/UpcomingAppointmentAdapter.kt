package com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemUpcomingAppointmentBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentItem
import com.app.gentlemanspa.utils.formatDayDate
import com.bumptech.glide.Glide


class UpcomingAppointmentAdapter(private val dataList:ArrayList<UpcomingServiceAppointmentItem>)  : RecyclerView.Adapter<UpcomingAppointmentAdapter.ViewHolder>()  {

    private  lateinit var upcomingAppointmentCallbacks:UpcomingAppointmentCallbacks
    class ViewHolder(val binding : ItemUpcomingAppointmentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUpcomingAppointmentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val  item=dataList[position]

        holder.binding.apply {
            tvServiceName.text="${item.userFirstName} ${item.userLastName}"
            tvSpecialist.text= item.serviceName
            tvBooking.text="BOOKING ID: ${item.orderId}"
            tvDate.text="${formatDayDate(item.slotDate)} at ${item.fromTime}"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.userImage).error(
                R.drawable.no_product).into(ivProfessional)
            btnMessage.setOnClickListener {
                upcomingAppointmentCallbacks.onItemMessageClick(item)
            }
        }
    }

  fun setUpcomingAppointmentCallbacks(onClick:UpcomingAppointmentCallbacks){
      upcomingAppointmentCallbacks=onClick
  }

  interface UpcomingAppointmentCallbacks{
      fun onItemMessageClick(item:UpcomingServiceAppointmentItem)
  }
}