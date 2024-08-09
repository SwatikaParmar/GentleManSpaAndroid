package com.app.gentlemanspa.ui.customerDashboard.fragment.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemPastCustomerBinding

class PastCustomerAdapter : RecyclerView.Adapter<PastCustomerAdapter.ViewHolder>()  {

    class ViewHolder(val binding : ItemPastCustomerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPastCustomerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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