package com.app.gentlemanspa.ui.customerDashboard.fragment.professionalTeam.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemServiceBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.professionalTeam.model.ServicesData
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.ServiceListItem
import com.app.gentlemanspa.utils.CommonFunctions.decimalRoundToInt
import com.bumptech.glide.Glide

class ProfessionalServicesAdapter(private var professionalServiceList: ArrayList<ServicesData>) : RecyclerView.Adapter<ProfessionalServicesAdapter.ViewHolder>() {
    private lateinit var professionalServiceCallbacks: ProfessionalServiceCallbacks
    class ViewHolder(val binding: ItemServiceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return professionalServiceList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = professionalServiceList[position]
        holder.binding.apply {
            tvServiceName.text = item.name
            tvTime.text = "${item.durationInMinutes} mins"
           // tvRupees.text = "$${decimalRoundToInt(item.listingPrice)}"
            tvRupees.text = "$${item.listingPrice}"
            tvLessRupees.text = "$${item.basePrice}"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE + item.serviceIconImage)
                .placeholder(
                    R.drawable.service_placeholder
                ).error(R.drawable.service_placeholder).into(ivService)


            if (item.isAddedinCart) {
                ivAddService.setImageResource(R.drawable.ic_checked)
                professionalServiceCallbacks.setData(item.spaServiceId)

            } else {
                ivAddService.setImageResource(R.drawable.ic_add)
            }


            root.setOnClickListener {
                professionalServiceCallbacks.rootService(item)
            }
            ivAddService.setOnClickListener {
                professionalServiceCallbacks.addService(item)
            }
        }

    }

    fun setOnProfessionalServiceCallbacks(onClick: ProfessionalServiceCallbacks) {
        professionalServiceCallbacks = onClick
    }

    interface ProfessionalServiceCallbacks {
        fun rootService(item: ServicesData)
        fun addService(item: ServicesData)
        fun setData(spaServiceId: Int)
    }
}


