package com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemUpcomingAppointmentBinding


class UpcomingAppointmentAdapter  : RecyclerView.Adapter<UpcomingAppointmentAdapter.ViewHolder>()  {

    class ViewHolder(val binding : ItemUpcomingAppointmentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUpcomingAppointmentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply {
            root.setOnClickListener {
            }
        }
    }


}