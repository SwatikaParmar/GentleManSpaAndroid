package com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemScheduleBinding
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.WeekDaysItem

class ScheduleAdapter(private var weekDaysList: ArrayList<WeekDaysItem>) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>()  {

    private lateinit var scheduleCallbacks :ScheduleCallbacks
    class ViewHolder(val binding : ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weekDaysList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = weekDaysList[position]
        holder.binding.apply {
            tvWeek.text = item.weekName
            root.setOnClickListener {
                scheduleCallbacks.rootSchedule(item)
            }
        }
    }

    fun setOnClickScheduleCallbacks(onClick : ScheduleCallbacks){
        scheduleCallbacks = onClick
    }


    interface ScheduleCallbacks{
        fun rootSchedule(item: WeekDaysItem)
    }


}