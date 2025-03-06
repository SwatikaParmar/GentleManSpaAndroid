package com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemBlockDatesBinding

class ExpertBlockDatesAdapter  ( private var dateList: List<String>) : RecyclerView.Adapter<ExpertBlockDatesAdapter.ViewHolder>()  {

    private lateinit var expertBlockDatesCallBacks : ExpertBlockDatesCallBacks
    class ViewHolder(val binding : ItemBlockDatesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBlockDatesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dateList.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dateList[position]
        holder.binding.apply {
            tvDate.text=item
            ivUnBlock.setOnClickListener{
                expertBlockDatesCallBacks.expertUnBlockDate(item)
            }
        }
    }

    fun setExpertBlockDatesCallBacks(onclick: ExpertBlockDatesCallBacks){
        expertBlockDatesCallBacks=onclick
    }

    interface ExpertBlockDatesCallBacks{
        fun expertUnBlockDate(date:String)
    }
}