package com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessionalService.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemSelectProfessionalBinding
import com.app.gentlemanspa.databinding.ItemSelectProfessionalServiceBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.GetCartItemsResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.Service
import com.app.gentlemanspa.utils.formatDayDate
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.bumptech.glide.Glide

class SelectProfessionalServiceAdapter(private var serviceList: List<Service>) : RecyclerView.Adapter<SelectProfessionalServiceAdapter.ViewHolder>() {

    private lateinit var selectProfessionalCallbacks : SelectProfessionalServiceCallbacks
    class ViewHolder(val binding : ItemSelectProfessionalServiceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSelectProfessionalServiceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // val item = professionalList[position]
         val item = serviceList[position]
         holder.binding.apply {
             tvServiceName.text = item.name
             if (item.slotDate!=null){
                 tvSpecialist.setVisible()
                 ivProfessionalName.setVisible()
                 tvSpecialist.text = "${formatDayDate(item.slotDate)} ${item.fromTime}"
                 Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.professionalImage).placeholder(R.drawable.professional_placeholder).error(R.drawable.professional_placeholder).into(ivProfessionalName)

             }else{
                 tvSpecialist.visibility= View.INVISIBLE
                 ivProfessionalName.visibility= View.INVISIBLE
             }
             tvProfessionalName.text = item.professionalName ?: "Any Professional"

             root.setOnClickListener {
                 selectProfessionalCallbacks.rootSelectProfessionalService(item)
             }
         }
    }

    fun setOnSelectProfessionalServiceCallbacks(onClick : SelectProfessionalServiceCallbacks) {
        selectProfessionalCallbacks = onClick
    }

    interface SelectProfessionalServiceCallbacks {
        fun rootSelectProfessionalService(item:Service)
    }
}