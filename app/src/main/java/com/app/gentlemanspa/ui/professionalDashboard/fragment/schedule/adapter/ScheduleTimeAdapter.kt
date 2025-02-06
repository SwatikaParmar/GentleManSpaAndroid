package com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemScheduleTimeBinding
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.SchedulesByProfessionalDetailWorkingTime

class ScheduleTimeAdapter ( private var workingTimeSchedulesList: List<SchedulesByProfessionalDetailWorkingTime>) : RecyclerView.Adapter<ScheduleTimeAdapter.ViewHolder>()  {

    private lateinit var scheduleTimeCallbacks :ScheduleTimeCallbacks
    class ViewHolder(val binding : ItemScheduleTimeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScheduleTimeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return workingTimeSchedulesList.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = workingTimeSchedulesList[position]
        holder.binding.apply {
            if (item.fromTime.isNotEmpty()  && item.toTime.isNotEmpty()){
                tvShiftTime.text = "${item.fromTime}-${item.toTime}"
            }else{
                Log.d("Schedule","fromTime->${item.fromTime} toTime->${item.toTime}")
            }
            ivEdit.setOnClickListener{
                scheduleTimeCallbacks.updateScheduleTime(position)
            }
            ivDelete.setOnClickListener{
                scheduleTimeCallbacks.deleteSchedule(position)
            }
        }
    }

    fun setOnClickScheduleTimeCallbacks(onClick : ScheduleTimeCallbacks){
        scheduleTimeCallbacks = onClick
    }


    interface ScheduleTimeCallbacks{
        fun updateScheduleTime( position:Int)
        fun deleteSchedule(position:Int)
    }


}