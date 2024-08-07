package com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.ItemTimeSlotsServiceBinding


class TimeSlotServiceAdapter: RecyclerView.Adapter<TimeSlotServiceAdapter.ViewHolder>()  {

    private var positionChecked: Int=0

    class ViewHolder(val binding : ItemTimeSlotsServiceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTimeSlotsServiceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if (position ==positionChecked){
                positionChecked =-1
                cvTime.strokeColor = ContextCompat.getColor(MyApplication.context, R.color.app_color)
                clTime.setBackgroundResource(R.color.app_color)
                val typeface = ResourcesCompat.getFont(MyApplication.context, R.font.poppins_medium)
                tvTime.setTypeface(typeface)
                //  selectSlotClick.getAvailableDates(item,position)

            }else{
                cvTime.strokeColor = ContextCompat.getColor(MyApplication.context, R.color.border_time_color)
                clTime.setBackgroundResource(R.color.bg_time_color)
                val typeface = ResourcesCompat.getFont(MyApplication.context, R.font.poppins)
                tvTime.setTypeface(typeface)
            }

            root.setOnClickListener {
                positionChecked =position
                if (position ==positionChecked) {
                    positionChecked = position
                    cvTime.strokeColor = ContextCompat.getColor(MyApplication.context, R.color.app_color)
                    val typeface = ResourcesCompat.getFont(MyApplication.context, R.font.poppins_medium)
                    tvTime.typeface = typeface
                    clTime.setBackgroundResource(R.color.app_color)
                    notifyDataSetChanged()
                }
                // selectSlotClick.getAvailableDates(item,position)

            }
        }
    }

    fun setOnClickDoctorsCallback(click : DoctorsCallback){
    }

    interface DoctorsCallback{
        fun rootDoctors()
    }
}