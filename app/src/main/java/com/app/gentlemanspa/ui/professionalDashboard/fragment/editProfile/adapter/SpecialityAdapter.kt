package com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemSpecialityBinding
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model.SpecialityItem

class SpecialityAdapter(
    private var specialityList: ArrayList<SpecialityItem>,
    var specialityIds: MutableList<String>?
) : RecyclerView.Adapter<SpecialityAdapter.ViewHolder>() {
    var selectedSpecialities: ArrayList<SpecialityItem> = ArrayList()

    class ViewHolder(val binding: ItemSpecialityBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSpecialityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return specialityList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = specialityList[position]

        holder.binding.apply {
            cbText.text = item.speciality
            cbText.isChecked = item.isChecked
            if (item.isChecked) {
                Log.d("selectedItems", "checked speciality name->${item.speciality}")

            }
            Log.d("selectedItems", "specialityIds->${specialityIds}")
            for (i in 0 until specialityIds?.size!!) {
                if (item.specialityId.toString() == specialityIds!![i]) {
                    cbText.isChecked = true
                    item.isChecked = true
                    selectedSpecialities.add(item)
                }
            }
            if (item.isChecked) {
                Log.d("selectedItems", "below checked speciality name->${item.speciality}")

            }

            cbText.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                 if (isChecked) {
                     selectedSpecialities.add(item)
                    // specialityIds?.add(item.specialityId.toString())

                 } else {
                     selectedSpecialities.remove(item)
                   //  specialityIds?.remove(item.specialityId.toString())
                 }
            }
        }

    }

    fun getSelectedItems(): ArrayList<SpecialityItem> {
         return selectedSpecialities
       // return specialityList
    }

}