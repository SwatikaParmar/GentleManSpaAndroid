package com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.base.MyApplication.Companion.context
import com.app.gentlemanspa.databinding.ItemProductPhotoBinding
import com.bumptech.glide.Glide
import java.io.File

class ProductPhotoAdapter(private var productPhoto: ArrayList<File>) : RecyclerView.Adapter<ProductPhotoAdapter.ViewHolder>(){
    class ViewHolder(val binding : ItemProductPhotoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductPhotoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(productPhoto[position]).into(holder.binding.addProfileBtn)
        holder.binding.ivRemove.setOnClickListener {
            productPhoto.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return productPhoto.size
    }
}