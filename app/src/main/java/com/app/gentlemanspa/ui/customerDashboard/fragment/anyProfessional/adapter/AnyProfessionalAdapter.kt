package com.app.gentlemanspa.ui.customerDashboard.fragment.anyProfessional.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemSelectProfessionalBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalItem
import com.bumptech.glide.Glide

class AnyProfessionalAdapter(private var professionalList: ArrayList<ProfessionalItem>) : RecyclerView.Adapter<AnyProfessionalAdapter.ViewHolder>() {
    private lateinit var anyProfessionalCallbacks :AnyProfessionalCallbacks
    class ViewHolder(val binding : ItemSelectProfessionalBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSelectProfessionalBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return professionalList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = professionalList[position]
        holder.binding.apply {
            tvServiceName.text = "${item.firstName} ${item.lastName}"
            val specialities= item.professionalDetail?.speciality?.map { it }
            val specialityName = specialities?.joinToString(",")
            tvSpecialist.text = specialityName
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +item.profilepic).placeholder(
                R.drawable.professional_placeholder).error(R.drawable.professional_placeholder).into(ivService)
            root.setOnClickListener {
                anyProfessionalCallbacks.rootAnyProfessional(item)
            }
        }
    }

    fun setOnSelectProfessionalCallbacks(onClick :AnyProfessionalCallbacks) {
        anyProfessionalCallbacks = onClick
    }

    interface AnyProfessionalCallbacks {
        fun rootAnyProfessional(item:ProfessionalItem)
    }

}
