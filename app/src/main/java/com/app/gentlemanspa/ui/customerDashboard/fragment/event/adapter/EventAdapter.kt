package com.app.gentlemanspa.ui.customerDashboard.fragment.event.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemEventBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.event.model.EventListData
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsListItem
import com.app.gentlemanspa.utils.formatDate
import com.bumptech.glide.Glide
import kotlin.coroutines.coroutineContext

class EventAdapter (val context:Context,val eventList:List<EventListData>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    private lateinit var eventCallbacks: EventCallbacks
    class ViewHolder(val binding : ItemEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val item=eventList[position]
        holder.binding.apply {
            tvEventName.text =item.title
            tvEventDate.text =formatDate(item.startDate)
            tvLocation.text = item.location
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +"").error(R.drawable.no_product).placeholder(
                R.drawable.no_product).into(ivEvent)

            if (item.isRegistered){
                btnRegister.text="Registered"
                btnRegister.backgroundTintList = ContextCompat.getColorStateList(context, R.color.dark_brown)
                btnRegister.strokeColor = ContextCompat.getColorStateList(context, R.color.dark_brown)
                btnRegister.setTextColor(ContextCompat.getColor(context, R.color.light_app_color))
            }else{
                btnRegister.text="Register"
                btnRegister.backgroundTintList = ContextCompat.getColorStateList(context, R.color.light_white)
                btnRegister.strokeColor = ContextCompat.getColorStateList(context, R.color.black)
                btnRegister.setTextColor(ContextCompat.getColor(context, R.color.black))

            }
            btnRegister.setOnClickListener{
              eventCallbacks.addOrUpdateEventRegistration(item)
            }
            ivEventLocation.setOnClickListener{
                eventCallbacks.redirectToEventLocation(item)
            }
        }
    }
    fun setEventCallbacks(onClick : EventCallbacks){
        eventCallbacks = onClick
    }

    interface EventCallbacks{
        fun addOrUpdateEventRegistration(item: EventListData)
        fun redirectToEventLocation(item: EventListData)
    }
}