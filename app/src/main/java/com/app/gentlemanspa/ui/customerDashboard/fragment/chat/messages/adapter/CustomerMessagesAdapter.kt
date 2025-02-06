package com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemMessagesBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.model.CustomerMessagesData
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.bumptech.glide.Glide

class CustomerMessagesAdapter (private var customerMessagesList: List<CustomerMessagesData>) : RecyclerView.Adapter<CustomerMessagesAdapter.ViewHolder>() {

    private lateinit var customerMessagesCallbacks: CustomerMessagesCallbacks
    class ViewHolder(val binding : ItemMessagesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessagesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return customerMessagesList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = customerMessagesList[position]
        holder.binding.apply {
            tvUserName.text="${item.firstName} ${item.lastName}"
            tvLastMessage.text=item.lastMessage
            if(item.onlineStatus){
                ivOnlineStatus.setVisible()
            }else{
                ivOnlineStatus.setGone()
            }
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.profilePic).placeholder(
                R.drawable.profile_placeholder).error(R.drawable.profile_placeholder).into(ivProfile)

            root.setOnClickListener {
                customerMessagesCallbacks.rootCustomerMessages(item)
            }
        }

    }

    fun setCustomerMessagesCallbacks(onClick : CustomerMessagesCallbacks) {
        customerMessagesCallbacks =onClick
    }

    interface CustomerMessagesCallbacks {
        fun rootCustomerMessages(item: CustomerMessagesData)
    }

}