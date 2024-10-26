package com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.ItemTimeSlotsServiceBinding
import com.app.gentlemanspa.ui.customerDashboard.fragment.anyProfessional.adapter.AnyProfessionalAdapter.AnyProfessionalCallbacks
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.model.Slot


class TimeSlotServiceAdapter(private var timeSlotList: ArrayList<Slot>): RecyclerView.Adapter<TimeSlotServiceAdapter.ViewHolder>()  {
    private lateinit var selectSlotClick : SelectSlotCallback
  //  private var positionChecked: Int=0
    private var positionChecked: Int=-1

    class ViewHolder(val binding : ItemTimeSlotsServiceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTimeSlotsServiceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return timeSlotList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val item=timeSlotList[position]
        holder.binding.apply {
            tvTimeSlot.text=item.fromTime
            Log.d("AvailableTimeResponse","inside adapter->$${item.fromTime} - ${item.toTime}")

            if (position ==positionChecked){
                positionChecked =-1
                cvTime.strokeColor = ContextCompat.getColor(MyApplication.context, R.color.yellow_color_background)
                clTime.setBackgroundResource(R.color.yellow_color_background)
                val typeface = ResourcesCompat.getFont(MyApplication.context, R.font.poppins_medium)
                tvTimeSlot.typeface = typeface
                //  selectSlotClick.getAvailableDates(item,position)

            }else{
                cvTime.strokeColor = ContextCompat.getColor(MyApplication.context, R.color.border_time_color)
                clTime.setBackgroundResource(R.color.bg_time_color)
                val typeface = ResourcesCompat.getFont(MyApplication.context, R.font.poppins)
                tvTimeSlot.typeface = typeface
            }

            root.setOnClickListener {
                positionChecked =position
                if (position ==positionChecked) {
                    positionChecked = position
                    cvTime.strokeColor = ContextCompat.getColor(MyApplication.context, R.color.app_color)
                    val typeface = ResourcesCompat.getFont(MyApplication.context, R.font.poppins_medium)
                    tvTimeSlot.typeface = typeface
                    clTime.setBackgroundResource(R.color.app_color)
                    notifyDataSetChanged()
                }
                 selectSlotClick.rootSelectSlot(item.slotId,item.fromTime)

            }
        }
    }

    fun setOnClickSlotCallback(onClick : SelectSlotCallback){
        selectSlotClick = onClick

    }

    interface SelectSlotCallback{
        fun rootSelectSlot(slotId:Int,slotTime:String)
    }
}