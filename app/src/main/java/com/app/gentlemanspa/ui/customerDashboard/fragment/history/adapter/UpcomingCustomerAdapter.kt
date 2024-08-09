package com.app.gentlemanspa.ui.customerDashboard.fragment.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemUpcomingCustomerBinding

class UpcomingCustomerAdapter : RecyclerView.Adapter<UpcomingCustomerAdapter.ViewHolder>()  {

    class ViewHolder(val binding : ItemUpcomingCustomerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUpcomingCustomerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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