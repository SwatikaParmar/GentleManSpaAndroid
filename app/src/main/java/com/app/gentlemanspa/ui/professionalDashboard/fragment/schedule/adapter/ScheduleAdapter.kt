package com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemScheduleBinding
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.SchedulesByProfessionalDetailData
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.WeekDaysItem
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setInvisible
import com.app.gentlemanspa.utils.setVisible

class ScheduleAdapter(private var weekDaysList: ArrayList<WeekDaysItem>,private var workingTimeSchedulesList: ArrayList<SchedulesByProfessionalDetailData>) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>()  {

    private lateinit var scheduleCallbacks :ScheduleCallbacks
    class ViewHolder(val binding : ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weekDaysList.size
     //   return minOf(weekDaysList.size, workingTimeSchedulesList.size)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = weekDaysList[position]
        val itemWorkingTimeList:SchedulesByProfessionalDetailData= if (position < workingTimeSchedulesList.size) {
            workingTimeSchedulesList[position]
        }else{
            SchedulesByProfessionalDetailData("","","",0,0,"",0)
        }
        holder.binding.apply {
            tvWeek.text = item.weekName

            Log.d("type","professionalScheduleId->${itemWorkingTimeList.professionalScheduleId}")
            if (itemWorkingTimeList.fromTime.isNotEmpty() && itemWorkingTimeList.toTime.isNotEmpty()){
               clWorking.setVisible()
                tvWorkingTime.text = "${itemWorkingTimeList.fromTime}-${itemWorkingTimeList.toTime}"
            }else{
                clWorking.setInvisible()
            }
            root.setOnClickListener {
                if (itemWorkingTimeList.fromTime.isNotEmpty() && itemWorkingTimeList.toTime.isNotEmpty()) {
                    scheduleCallbacks.rootSchedule(item,"Update",itemWorkingTimeList.professionalScheduleId)
                }else{
                    scheduleCallbacks.rootSchedule(item,"Create",itemWorkingTimeList.professionalScheduleId)

                }
            }
        }
    }

    fun setOnClickScheduleCallbacks(onClick : ScheduleCallbacks){
        scheduleCallbacks = onClick
    }


    interface ScheduleCallbacks{
        fun rootSchedule(item: WeekDaysItem,type:String,professionalScheduleId:Int)
    }


}