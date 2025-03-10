package com.app.gentlemanspa.ui.professionalDashboard.fragment.professionalRequests.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemProfessionalRequestsBinding
import com.app.gentlemanspa.ui.professionalDashboard.fragment.professionalRequests.model.ProfessionalRequestsData

class ProfessionalRequestsAdapter(private var dataList: List<ProfessionalRequestsData>) :
    RecyclerView.Adapter<ProfessionalRequestsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemProfessionalRequestsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProfessionalRequestsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.apply {
            tvTittle.text = item.requestType
            tvDescription.text = item.description
            tvStatus.text = item.status
            when (item.status.trim()) {
                "Approved"-> {
                    tvStatus.setTextColor(Color.GREEN)
                }
                "Pending"-> {
                    tvStatus.setTextColor(Color.BLACK)
                }
                "Rejected"-> {
                    tvStatus.setTextColor(Color.RED)
                }else->{
                    Log.d("textColor","Something went wrong status ->${item.status}")
                }
            }
        }
    }
}


