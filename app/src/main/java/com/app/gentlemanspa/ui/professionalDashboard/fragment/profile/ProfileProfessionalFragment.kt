package com.app.gentlemanspa.ui.professionalDashboard.fragment.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.FragmentEditProfileProfessionalBinding
import com.app.gentlemanspa.databinding.FragmentProfileProfessionalBinding
import com.app.gentlemanspa.network.ApiConstants.BASE_FILE
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.auth.fragment.login.LoginFragmentDirections
import com.app.gentlemanspa.ui.auth.fragment.login.viewModel.LoginViewModel
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.viewModel.ProfileProfessionalDetailViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide

class ProfileProfessionalFragment : Fragment(), View.OnClickListener {
    private lateinit var binding : FragmentProfileProfessionalBinding
    private val viewModel: ProfileProfessionalDetailViewModel by viewModels { ViewModelFactory(InitialRepository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileProfessionalBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.onClick = this
        viewModel.getProfessionalDetail()
    }


    @SuppressLint("SetTextI18n")
    private fun initObserver() {
        viewModel.resultProfileProfessionalDetailAccount.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()

                        AppPrefs(requireContext()).setProfileProfessionalData("PROFILE_DATA",it.data)
                        binding.tvName.text ="${it.data?.data?.firstName} ${it.data?.data?.lastName}"
                        binding.tvPhone.text =it.data?.data?.phoneNumber
                        Glide.with(requireContext()).load(BASE_FILE+it.data?.data?.profilepic).into(binding.ivProfile)

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        MyApplication.hideProgress()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.ivEditProfile ->{
                findNavController().navigate(R.id.action_profileProfessionalFragment_to_editProfileProfessionalFragment)
            }
        }
    }

}