package com.app.gentlemanspa.ui.professionalDashboard.fragment.myService.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemMyServiceBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.ServiceListItem
import com.app.gentlemanspa.ui.professionalDashboard.fragment.myService.model.MyServiceItem
import com.app.gentlemanspa.ui.professionalDashboard.fragment.myService.model.ProfessionalServiceResponse
import com.bumptech.glide.Glide

class MyServiceAdapter (private var serviceList: ArrayList<MyServiceItem>) : RecyclerView.Adapter<MyServiceAdapter.ViewHolder>() {

    private lateinit var myServiceCallbacks : MyServiceCallbacks
    class ViewHolder(val binding : ItemMyServiceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyServiceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = serviceList[position]
        holder.binding.apply {
            tvServiceName.text = item.name
            tvTime.text = "${item.durationInMinutes} mins"
            tvDescription.text =  item.description
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.serviceIconImage).placeholder(
                R.drawable.service_placeholder).error(R.drawable.service_placeholder).into(ivService)
            root.setOnClickListener {
              //  myServiceCallbacks.rootService(item)
            }

        }

    }

    fun setOnMyServiceCallbacks(onClick :MyServiceCallbacks) {
        myServiceCallbacks = onClick
    }

    interface MyServiceCallbacks{
        fun rootService(item: MyServiceItem)
    }



}