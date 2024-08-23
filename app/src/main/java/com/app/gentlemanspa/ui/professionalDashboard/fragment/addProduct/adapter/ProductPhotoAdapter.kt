package com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.context
import com.app.gentlemanspa.databinding.ItemProductPhotoBinding
import com.app.gentlemanspa.databinding.ItemProductProfessionalBinding
import com.app.gentlemanspa.databinding.ItemStaticProductBinding
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.AddPhotoRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.product.adapter.ProductProfessionalAdapter.ViewHolder
import com.app.gentlemanspa.utils.setGone
import com.app.gentlemanspa.utils.setVisible
import com.bumptech.glide.Glide
import java.io.File

class ProductPhotoAdapter(private var productPhoto: ArrayList<AddPhotoRequest>) : RecyclerView.Adapter<ProductPhotoAdapter.ViewHolder>(){

    private lateinit var uploadProductCallback: UploadProductCallback
    class ViewHolder(val binding : ItemProductPhotoBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position ==0){
            holder.binding.apply {
                Glide.with(context).load(productPhoto[position].staticImage).into(plusBtn)
                plusBtn.setVisible()
                addProfileBtn.setGone()
                clRemove.setGone()

                root.setOnClickListener {
                    uploadProductCallback.rootUploadProduct()
                }

            }


        }else{

            holder.binding.apply {
                Glide.with(context).load(productPhoto[position].image).into(addProfileBtn)
                plusBtn.setGone()
                addProfileBtn.setVisible()
                clRemove.setVisible()

                holder.binding.ivRemove.setOnClickListener {
                    productPhoto.removeAt(position)
                    notifyDataSetChanged()
                }

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductPhotoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return productPhoto.size
    }


    fun setOnClickUploadProduct(click : UploadProductCallback){
        uploadProductCallback = click
    }



    interface UploadProductCallback {
        fun rootUploadProduct()
    }

}