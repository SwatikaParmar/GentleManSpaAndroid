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

class ScheduleAdapter(
    private var weekDaysList: ArrayList<WeekDaysItem>,
    private var workingTimeSchedulesList: ArrayList<SchedulesByProfessionalDetailData>
) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    private lateinit var scheduleCallbacks: ScheduleCallbacks

    class ViewHolder(val binding: ItemScheduleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weekDaysList.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = weekDaysList[position]
        Log.d("test", "workingTimeSchedulesList size->${workingTimeSchedulesList.size} data->$workingTimeSchedulesList")
        Log.d("test", "weekDaysList size->${weekDaysList.size} data->$weekDaysList")

        // Find the index of the corresponding schedule for this week day
        val itemWorkingTimeList = workingTimeSchedulesList.firstOrNull {
            it.weekdaysId == item.weekdaysId
        } ?: SchedulesByProfessionalDetailData(
            ArrayList(),
            0,
            0,
            0,
            "",
            ArrayList()
        )  // Default empty schedule if no match found

        holder.binding.apply {
            tvDayName.text = item.weekName
            if (itemWorkingTimeList != null && itemWorkingTimeList.workingTime.size >= 2) {
                btnAdd.setInvisible()
            } else {
                btnAdd.setVisible()
            }
            if (itemWorkingTimeList != null && itemWorkingTimeList.workingTime.isNotEmpty()) {
                val scheduleTimeAdapter = ScheduleTimeAdapter(itemWorkingTimeList.workingTime)
                rvScheduleTime.adapter = scheduleTimeAdapter
                scheduleTimeAdapter.setOnClickScheduleTimeCallbacks(object :ScheduleTimeAdapter.ScheduleTimeCallbacks{
                    override fun updateScheduleTime(position: Int) {

                       if (itemWorkingTimeList.workingTime.size>1){
                           Log.d("update","inside if of update")
                           when(position){
                               0->{
                                   Log.d("position","position 0 clicked")
                                   val oldFromTime = itemWorkingTimeList.workingTime[1].fromTime
                                   val oldToTime = itemWorkingTimeList.workingTime[1].toTime
                                   scheduleCallbacks.addUpdateSchedule(item, "Update", itemWorkingTimeList.professionalScheduleId, oldFromTime, oldToTime)
                               }
                               1->{
                                   val oldFromTime = itemWorkingTimeList.workingTime[0].fromTime
                                   val oldToTime = itemWorkingTimeList.workingTime[0].toTime
                                   scheduleCallbacks.addUpdateSchedule(item, "Update", itemWorkingTimeList.professionalScheduleId, oldFromTime, oldToTime)

                               }
                               else->{
                               Log.d("position","position else clicked")

                               }
                           }
                       }else{
                           Log.d("update","inside else of update")
                           scheduleCallbacks.addUpdateSchedule(item, "Update", itemWorkingTimeList.professionalScheduleId, "", "")

                       }
                    }

                    override fun deleteSchedule(position: Int) {
                        if(itemWorkingTimeList.workingTime.size>1){
                            when(position){
                                0->{
                                    Log.d("position","position 0 clicked")
                                    val oldFromTime = itemWorkingTimeList.workingTime[1].fromTime
                                    val oldToTime = itemWorkingTimeList.workingTime[1].toTime
                                    scheduleCallbacks.deleteSchedule(item, "delete", itemWorkingTimeList.professionalScheduleId, oldFromTime, oldToTime)
                                }
                                1->{
                                    val oldFromTime = itemWorkingTimeList.workingTime[0].fromTime
                                    val oldToTime = itemWorkingTimeList.workingTime[0].toTime
                                    scheduleCallbacks.deleteSchedule(item, "delete", itemWorkingTimeList.professionalScheduleId, oldFromTime, oldToTime)

                                }
                                else->{
                                    Log.d("position","position else clicked")

                                }
                            }
                        }else{
                            scheduleCallbacks.deleteSchedule(item, "delete", itemWorkingTimeList.professionalScheduleId, "", "")
                        }

                    }
                })
            }


            btnAdd.setOnClickListener {
                Log.d("workingTime","workingTime size${itemWorkingTimeList.workingTime.size}")
                if (itemWorkingTimeList != null && itemWorkingTimeList.workingTime.isNotEmpty()) {
                    val oldFromTime = itemWorkingTimeList.workingTime[0].fromTime
                    val oldToTime = itemWorkingTimeList.workingTime[0].toTime
                    scheduleCallbacks.addUpdateSchedule(item, "Create", itemWorkingTimeList.professionalScheduleId, oldFromTime, oldToTime)
                } else {
                    scheduleCallbacks.addUpdateSchedule(item, "Create", 0, "", "")
                }

            }

        }
    }

    fun setOnClickScheduleCallbacks(onClick: ScheduleCallbacks) {
        scheduleCallbacks = onClick
    }


    interface ScheduleCallbacks {
        fun addUpdateSchedule(item: WeekDaysItem, type: String, professionalScheduleId: Int,oldFromTime:String,oldToTime:String)
        fun deleteSchedule(item: WeekDaysItem, type: String, professionalScheduleId: Int,oldFromTime:String,oldToTime:String)
    }
}