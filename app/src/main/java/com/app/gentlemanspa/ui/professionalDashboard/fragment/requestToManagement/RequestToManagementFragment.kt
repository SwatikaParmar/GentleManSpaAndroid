package com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.app.gentlemanspa.databinding.FragmentRequestToManagementBinding
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.utils.showToast


class RequestToManagementFragment : Fragment(), View.OnClickListener {
   lateinit var binding:FragmentRequestToManagementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       if(!this::binding.isInitialized){
           binding=FragmentRequestToManagementBinding.inflate(layoutInflater,container,false)
       }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.onClick=this
        val items = listOf("Maintenance", "Business", "Personal")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            //    val selectedItem = parent.getItemAtPosition(position) as SpinnerItem
                requireContext().showToast( "Selected: $position")
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where nothing is selected if needed
            }
        }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.ivDrawer->{
                (activity as ProfessionalActivity).isDrawer(true)
            }
            binding.btnRequest->{

            }
        }
    }
    private fun initObserver() {

    }
}