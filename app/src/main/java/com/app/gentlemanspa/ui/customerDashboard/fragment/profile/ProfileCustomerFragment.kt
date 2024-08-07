package com.app.gentlemanspa.ui.customerDashboard.fragment.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.FragmentProfileCustomerBinding
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity


class ProfileCustomerFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentProfileCustomerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (!this::binding.isInitialized) {
            binding = FragmentProfileCustomerBinding.inflate(layoutInflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as CustomerActivity).bottomNavigation(true)
        initUI()
    }

    private fun initUI() {
        binding.onClick = this
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.clProfile ->{
                val action = ProfileCustomerFragmentDirections.actionProfileCustomerFragmentToEditProfileCustomerFragment()
                findNavController().navigate(action)
            }
        }
    }


}