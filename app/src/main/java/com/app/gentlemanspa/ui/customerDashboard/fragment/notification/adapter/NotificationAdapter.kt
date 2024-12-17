package com.app.gentlemanspa.ui.customerDashboard.fragment.notification.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemNotificationBinding


class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    private lateinit var notificationCallbacks: NotificationCallbacks
    class ViewHolder(val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 5
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply {
            tvTitle.text="Your appointment is booked successfully"
            tvDescription.text="Lorem ipsum is a dummy or placeholder text commonly used"
         /*   Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE + item.serviceIconImage)
                .placeholder(
                    R.drawable.ic_notification
                ).error(R.drawable.service_placeholder).into(ivNotification)*/



            root.setOnClickListener {
            }

        }

    }

    fun setOnNotificationCallbacks(onClick: NotificationCallbacks) {
        notificationCallbacks = onClick
    }

    interface NotificationCallbacks {
        fun rootService(item:String)
    }
}
