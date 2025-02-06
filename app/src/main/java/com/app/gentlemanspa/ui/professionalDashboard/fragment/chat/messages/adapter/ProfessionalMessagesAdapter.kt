package com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.messages.adapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemMessagesBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.adapter.CustomerMessagesAdapter
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.model.CustomerMessagesData
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class ProfessionalMessagesAdapter(private var customerMessagesList: List<CustomerMessagesData>) : RecyclerView.Adapter<ProfessionalMessagesAdapter.ViewHolder>() {

    private lateinit var professionalMessagesCallbacks: ProfessionalMessagesCallbacks
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
                professionalMessagesCallbacks.rootProfessionalMessages(item)
            }
        }

    }

    fun setProfessionalMessagesCallbacks(onClick : ProfessionalMessagesCallbacks) {
        professionalMessagesCallbacks =onClick
    }

    interface ProfessionalMessagesCallbacks {
        fun rootProfessionalMessages(item: CustomerMessagesData)
    }

}