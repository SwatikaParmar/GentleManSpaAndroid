package com.app.gentlemanspa.ui.professionalDashboard.fragment.messages.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemMessagesBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.messages.adapter.MessagesAdapter
import com.bumptech.glide.Glide

class ProfessionalMessagesAdapter : RecyclerView.Adapter<ProfessionalMessagesAdapter.ViewHolder>() {
    private lateinit var professionalMessagesCallbacks: ProfessionalMessagesCallbacks

    class ViewHolder(val binding: ItemMessagesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMessagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 10
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
        /*    tvName.text = "Kim"
            tvMessage.text = "hello"
            tvTime.text = "34 mins"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE + "0").error(
                R.drawable.profile_placeholder
            ).into(ivProfile)
            root.setOnClickListener {
                professionalMessagesCallbacks.onProfessionalMessageItemClick()
            }*/
        }
    }

    fun setProfessionalMessagesCallbacks(onClick: ProfessionalMessagesCallbacks) {
        professionalMessagesCallbacks = onClick
    }

    interface ProfessionalMessagesCallbacks {
        fun onProfessionalMessageItemClick()
    }
}