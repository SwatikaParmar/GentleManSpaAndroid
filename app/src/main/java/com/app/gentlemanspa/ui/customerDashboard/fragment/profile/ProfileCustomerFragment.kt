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
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.model.GetProfessionalDetailResponse
import com.app.gentlemanspa.utils.AppPrefs
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
        profileCustomerData= AppPrefs(requireContext()).getProfileCustomerData("PROFILE_DATA")
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
                val action=ProfileCustomerFragmentDirections.actionProfileCustomerFragmentToMessagesFragment()
                findNavController().navigate(action)
            }
            binding.clEvent ->{
                val action=ProfileCustomerFragmentDirections.actionProfileCustomerFragmentToEvenFragment()
                findNavController().navigate(action)
            }
            binding.clRefer->{
              refer()
            }
        }
    }

    private fun refer() {
        val url = "https://www.testUrl.com"
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"  // Set MIME type for text content
            putExtra(Intent.EXTRA_TEXT, url)  // Add the URL as the text to share
        }
        startActivity(Intent.createChooser(shareIntent, "Share URL via"))
    }


}