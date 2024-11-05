package com.app.gentlemanspa.ui.customerDashboard.fragment.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.FragmentEventBinding
import com.app.gentlemanspa.databinding.ItemEventBinding
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.event.adapter.EventAdapter
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity


class EventFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        if (!this::binding.isInitialized){
            binding=FragmentEventBinding.inflate(layoutInflater,container,false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as CustomerActivity).bottomNavigation(false)
        binding.onClick=this
        setEventAdapter()
    }

    private fun setEventAdapter() {
        val eventAdapter = EventAdapter()
        binding.rvEvents.adapter=eventAdapter
    }

    override fun onClick(v: View) {
        when(v){
            binding.ivArrowBack ->{
                findNavController().popBackStack()
            }
        }
    }

}