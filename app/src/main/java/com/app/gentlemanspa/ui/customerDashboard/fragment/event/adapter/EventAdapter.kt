package com.app.gentlemanspa.ui.customerDashboard.fragment.event.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.ItemEventBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsListItem
import com.bumptech.glide.Glide

class EventAdapter () : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    private lateinit var productsCallbacks: EventCallbacks

    class ViewHolder(val binding : ItemEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 10
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply {
            tvEventName.text ="Beauty Show"
            tvEventDate.text = "20 Sept - 20 Oct "
            tvLocation.text = "New York"
            Glide.with(holder.itemView.context).load(ApiConstants.BASE_FILE +"").error(R.drawable.no_product).placeholder(
                R.drawable.no_product).into(ivEvent)

            root.setOnClickListener {
            }


        }

    }

    fun setOnClickEvent(onClick : EventCallbacks){
        productsCallbacks = onClick
    }

    interface EventCallbacks{
        fun rootProducts(item: ProductsListItem)

    }

}