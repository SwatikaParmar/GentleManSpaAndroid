package com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemAvailableDatesBinding

class ExpertAvailableDatesAdapter( private var dateList: List<String>) : RecyclerView.Adapter<ExpertAvailableDatesAdapter.ViewHolder>()  {

    private lateinit var expertAvailableDatesCallbacks : ExpertAvailableDatesCallbacks
    class ViewHolder(val binding : ItemAvailableDatesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAvailableDatesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dateList.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dateList[position]
        holder.binding.apply {
            tvDate.text = item
            tvBlock.setOnClickListener {
                expertAvailableDatesCallbacks.expertBlockDate(item)
            }
        }
    }

    fun setExpertAvailableDatesCallbacks(onClick: ExpertAvailableDatesCallbacks){
        expertAvailableDatesCallbacks=onClick
    }
    interface ExpertAvailableDatesCallbacks{
        fun expertBlockDate(date:String)
    }
}