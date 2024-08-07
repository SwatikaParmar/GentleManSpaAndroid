package com.app.gentlemanspa.ui.auth.fragment.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.FragmentSplashBinding
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.utils.AppPrefs


class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        lifecycleScope.launchWhenResumed {
            Handler(Looper.getMainLooper()).postDelayed({
                val token = AppPrefs(requireContext()).getString("TOKEN").toString()
                val role = AppPrefs(requireContext()).getString("ROLE").toString()

                when {
                    token.isNotEmpty() && role == "Customer" -> {
                        startActivity(Intent(requireContext(), CustomerActivity::class.java))
                        requireActivity().finishAffinity()
                    }

                    token.isNotEmpty() && role == "Professional" -> {
                        startActivity(
                            Intent(
                                requireContext(),
                                ProfessionalActivity::class.java
                            )
                        )
                        requireActivity().finishAffinity()

                    }

                    else -> {

                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

                    }
                }


            }, 1500)

        }
    }


}