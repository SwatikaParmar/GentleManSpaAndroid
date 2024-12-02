package com.app.gentlemanspa.ui.customerDashboard.fragment.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.FragmentProfileCustomerBinding
import com.app.gentlemanspa.network.ApiConstants
import com.app.gentlemanspa.ui.chat.activity.ChatActivity
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.model.GetProfessionalDetailResponse
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.PROFESSIONAL_USER_ID
import com.app.gentlemanspa.utils.PROFILE_CUSTOMER_DATA
import com.app.gentlemanspa.utils.USER_ID
import com.app.gentlemanspa.utils.share
import com.bumptech.glide.Glide


class ProfileCustomerFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentProfileCustomerBinding
    private var profileCustomerData: GetProfessionalDetailResponse? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        binding.onClick = this
        profileCustomerData= AppPrefs(requireContext()).getProfileCustomerData(PROFILE_CUSTOMER_DATA)
        binding.tvName.text="${profileCustomerData?.data?.firstName} ${profileCustomerData?.data?.lastName}"
        binding.tvPhone.text=profileCustomerData?.data?.phoneNumber
        Glide.with(requireContext()).load(ApiConstants.BASE_FILE +profileCustomerData?.data?.profilepic).into(binding.ivProfile)

    }

    override fun onClick(v: View?) {
        when(v) {
            binding.clProfile ->{
                val action = ProfileCustomerFragmentDirections.actionProfileCustomerFragmentToEditProfileCustomerFragment()
                findNavController().navigate(action)
            }
            binding.clMessages->{
            /*    val action=ProfileCustomerFragmentDirections.actionProfileCustomerFragmentToMessagesFragment()
                findNavController().navigate(action)*/
                val intent=Intent(requireContext(), ChatActivity::class.java)
                intent.putExtra("userId","${AppPrefs(requireContext()).getStringPref(
                    USER_ID
                )}")
                startActivity(intent)
            }
            binding.clEvent ->{
                val action=ProfileCustomerFragmentDirections.actionProfileCustomerFragmentToEvenFragment()
                findNavController().navigate(action)
            }
            binding.clRefer->{
                val url = "https://www.testUrl.com"
                share(requireContext(),url)
            }
        }
    }




}