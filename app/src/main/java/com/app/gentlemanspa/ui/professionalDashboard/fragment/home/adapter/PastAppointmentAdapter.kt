package com.app.gentlemanspa.ui.professionalDashboard.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemPastAppointmentBinding

class PastAppointmentAdapter : RecyclerView.Adapter<PastAppointmentAdapter.ViewHolder>()  {

    class ViewHolder(val binding : ItemPastAppointmentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPastAppointmentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply {
            tvConfirmed.text ="Completed"
            root.setOnClickListener {
            }
        }
    }


}