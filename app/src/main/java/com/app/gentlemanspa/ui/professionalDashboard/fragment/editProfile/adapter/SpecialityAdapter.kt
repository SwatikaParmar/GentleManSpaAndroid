package com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.gentlemanspa.databinding.ItemSpecialityBinding
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model.SpecialityItem

class SpecialityAdapter(
    private var specialityList: ArrayList<SpecialityItem>,
    var specialityIds: List<String>?
) : RecyclerView.Adapter<SpecialityAdapter.ViewHolder>() {
    var selectedSpecialities: ArrayList<SpecialityItem> = ArrayList()
    class ViewHolder(val binding : ItemSpecialityBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSpecialityBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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

          for (i in 0 until specialityIds?.size!!) {
              if (item.specialityId.toString() == specialityIds!![i]){
                  cbText.isChecked = true
                  item.isChecked = true
                  selectedSpecialities.add(item)
              }
          }

            cbText.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                if (isChecked) {
                    selectedSpecialities.add(item)
                } else {
                    selectedSpecialities.remove(item)
                }
            }
        }

    }

    fun getSelectedItems(): ArrayList<SpecialityItem> {
        return selectedSpecialities
    }

}