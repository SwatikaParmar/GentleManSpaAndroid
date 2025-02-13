package com.app.gentlemanspa.ui.customerDashboard.fragment.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.databinding.FragmentProfileCustomerBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.network.ApiConstants.BASE_FILE
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.viewModel.HomeCustomerViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.profile.viewModel.ProfileCustomerViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.model.GetProfessionalDetailResponse
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CUSTOMER_USER_ID
import com.app.gentlemanspa.utils.PROFILE_CUSTOMER_DATA
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.share
import com.app.gentlemanspa.utils.showToast
import com.bumptech.glide.Glide


class ProfileCustomerFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentProfileCustomerBinding
    private var profileCustomerData: GetProfessionalDetailResponse? = null

    private val viewModel: ProfileCustomerViewModel by viewModels {
        ViewModelFactory(
            InitialRepository()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        Log.d("id", "customerId:${AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID)}")
        binding.onClick = this
        viewModel.userId.set(AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID).toString())
        viewModel.getCustomerDetail()
        profileCustomerData =
            AppPrefs(requireContext()).getProfileCustomerData(PROFILE_CUSTOMER_DATA)
        binding.tvName.text =
            "${profileCustomerData?.data?.firstName} ${profileCustomerData?.data?.lastName}"
        binding.tvPhone.text = profileCustomerData?.data?.email
        Glide.with(requireContext()).load(BASE_FILE + profileCustomerData?.data?.profilepic)
            .error(R.drawable.profile_placeholder).placeholder(R.drawable.profile_placeholder)
            .into(binding.ivProfile)

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.clProfile -> {
                val action =
                    ProfileCustomerFragmentDirections.actionProfileCustomerFragmentToEditProfileCustomerFragment()
                findNavController().navigate(action)
            }

            binding.clMessages -> {
                val customerUserId = "${AppPrefs(requireContext()).getStringPref(CUSTOMER_USER_ID)}"
                val action =
                    ProfileCustomerFragmentDirections.actionProfileCustomerFragmentToCustomerMessagesFragment(
                        customerUserId
                    )
                findNavController().navigate(action)
            }

            binding.clEvent -> {
                val action =
                    ProfileCustomerFragmentDirections.actionProfileCustomerFragmentToEvenFragment()
                findNavController().navigate(action)
            }

            binding.clRefer -> {
                val url = "https://www.testUrl.com"
                share(requireContext(), url)
            }
        }
    }

    private fun initObserver() {
        viewModel.resultProfileCustomerDetail.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        //      showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        AppPrefs(requireContext()).setProfileCustomerData(
                            PROFILE_CUSTOMER_DATA,
                            it.data
                        )
                        val name = "${it.data?.data?.firstName} ${it.data?.data?.lastName}"
                        val email = it.data?.data?.email.toString()
                        binding.tvName.text = name
                        binding.tvPhone.text = email
                        Glide.with(requireContext())
                            .load(ApiConstants.BASE_FILE + it.data?.data?.profilepic)
                            .error(R.drawable.profile_placeholder)
                            .placeholder(R.drawable.profile_placeholder).into(binding.ivProfile)
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
    }

}