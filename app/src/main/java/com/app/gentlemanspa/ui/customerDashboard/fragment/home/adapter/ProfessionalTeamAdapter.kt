package com.app.gentlemanspa.ui.customerDashboard.fragment.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemProfessionalTeamBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.CategoriesItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalItem
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalResponse
import com.bumptech.glide.Glide

class ProfessionalTeamAdapter (private var professionalTeamList: ArrayList<ProfessionalItem>) : RecyclerView.Adapter<ProfessionalTeamAdapter.ViewHolder>() {

    private lateinit var professionalTeamCallbacks: ProfessionalTeamCallbacks
    class ViewHolder(val binding : ItemProfessionalTeamBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProfessionalTeamBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return professionalTeamList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = professionalTeamList[position]
        holder.binding.apply {
            tvProfessionalName.text =  "${item.firstName} ${item.lastName}"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.profilepic).placeholder(
                R.drawable.service_placeholder).error(R.drawable.service_placeholder).into(ivProfessional)

            root.setOnClickListener {
              //  professionalTeamCallbacks.rootProfessionalTeam(item,position)
            }
        }

    }

    fun setOnProfessionalTeamCallbacks(onClick : ProfessionalTeamCallbacks) {
        professionalTeamCallbacks =onClick
    }

    interface ProfessionalTeamCallbacks {
        fun rootProfessionalTeam(item: CategoriesItem, position: Int)
    }

}