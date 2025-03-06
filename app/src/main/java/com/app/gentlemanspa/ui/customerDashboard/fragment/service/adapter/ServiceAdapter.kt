package com.app.gentlemanspa.ui.customerDashboard.fragment.service.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.context
import com.app.gentlemanspa.databinding.ItemServiceBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.ServiceListItem
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.CommonFunctions.decimalRoundToInt
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.bumptech.glide.Glide

class ServiceAdapter(private var serviceList: ArrayList<ServiceListItem>) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    private lateinit var serviceCallbacks : ServiceCallbacks
    class ViewHolder(val binding : ItemServiceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

         val item = serviceList[position]
         holder.binding.apply {
             tvServiceName.text = item.name
             tvTime.text = "${item.durationInMinutes} mins"
            /* tvRupees.text = "$${String.format("%.2f", item.listingPrice!!.toDouble())}"
             tvLessRupees.text =  "$${String.format("%.2f",item.basePrice!!.toDouble())}"*/
             tvRupees.text ="$${item.listingPrice?.toInt()}"
             tvLessRupees.text = "$${item.basePrice?.toInt()}"
             Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.serviceIconImage).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(ivService)

             /*    if (item.status ==true){
                          tvAddCart.text="Added"
                          cbIcon.setVisible()
                          clAddCart.background = ContextCompat.getDrawable(context,R.drawable.bg_black_button)
                      }else{
                          tvAddCart.text="Add To Cart"
                          cbIcon.setGone()
                          clAddCart.background = ContextCompat.getDrawable(context,R.drawable.bg_app_color)
                  }*/
             Log.d("service","inside adapter isAddedinCart->${item.isAddedinCart} spaServiceId->${item.spaServiceId} spaDetailId->${item.spaDetailId} ")

             if (item.isAddedinCart){
                 ivAddService.setImageResource(R.drawable.ic_checked)
             }else{
                 ivAddService.setImageResource(R.drawable.ic_add)
             }



             root.setOnClickListener {
                 serviceCallbacks.rootService(item)
             }
             ivAddService.setOnClickListener {
                 serviceCallbacks.addService(item)
             }
         }

    }

    fun setOnServiceCallbacks(onClick :ServiceCallbacks) {
        serviceCallbacks = onClick
    }

    interface ServiceCallbacks{
        fun rootService(item: ServiceListItem)
        fun addService(item: ServiceListItem)
    }



}