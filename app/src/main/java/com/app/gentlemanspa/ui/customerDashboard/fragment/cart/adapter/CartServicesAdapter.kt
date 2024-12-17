package com.app.gentlemanspa.ui.customerDashboard.fragment.cart.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemCartServicesBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.Service
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.ServiceListItem
import com.app.gentlemanspa.utils.CommonFunctions.decimalRoundToInt
import com.app.gentlemanspa.utils.formatDayDate
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.bumptech.glide.Glide

class CartServicesAdapter(private var serviceList: List<Service>) :
    RecyclerView.Adapter<CartServicesAdapter.ViewHolder>() {
    private lateinit var cartServicesCallbacks: CartServicesCallbacks

    class ViewHolder(val binding: ItemCartServicesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCartServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = serviceList[position]
        holder.binding.apply {
            tvServiceName.text = item.name
            tvTime.text = "${item.durationInMinutes} mins"
            /* tvRupees.text = String.format("%.2f", item.listingPrice.toDouble())
             tvLessRupees.text = String.format("%.2f",item.basePrice.toDouble())*/
            tvRupees.text = "$${item.listingPrice}"
            tvLessRupees.text = "$${item.basePrice}"
            Log.d("slotDate", "slotDate->${item.slotDate}")
            if (item.slotDate != null) {
                tvSlotDateTime.setVisible()
                tvSlotDateTime.text = "${formatDayDate(item.slotDate)} at ${item.fromTime}"
                tvSelectDateTime.text = "Reschedule"

            } else {
                tvSlotDateTime.setGone()
                tvSelectDateTime.text = "Select date and time"


            }
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE + item.serviceIconImage)
                .placeholder(
                    R.drawable.service_placeholder
                ).error(R.drawable.service_placeholder).into(ivService)




            root.setOnClickListener {
                cartServicesCallbacks.rootService(item)
            }
            ivAddService.setOnClickListener {
                cartServicesCallbacks.addOrRemoveService(item)
            }
            tvSelectDateTime.setOnClickListener {
                if (item.slotDate != null) {
                    cartServicesCallbacks.addSlot(item,"update")
                }else{
                    cartServicesCallbacks.addSlot(item,"add")

                }
            }
        }

    }

    fun setOnCartServicesCallbacks(onClick: CartServicesCallbacks) {
        cartServicesCallbacks = onClick
    }

    interface CartServicesCallbacks {
        fun rootService(item: Service)
        fun addOrRemoveService(item: Service)
        fun addSlot(item: Service, status:String)
    }

}