package com.app.gentlemanspa.ui.common.aboutUs

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.databinding.FragmentAboutUsBinding
import com.app.gentlemanspa.network.ApiConstants.BASE
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.ROLE
import kotlin.math.truncate


class AboutUsFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentAboutUsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutUsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.onClick = this
        if (AppPrefs(requireContext()).getStringPref(ROLE).toString() == "Professional") {
            (activity as ProfessionalActivity).bottomNavigation(false)
        } else {
            (activity as CustomerActivity).bottomNavigation(false)
        }
        (BASE + "Content/GetAboutHtml").also {
            loadUrlInWebView(it)
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadUrlInWebView(url: String) {
        val webSettings = binding.wvAboutUs.settings
        webSettings.javaScriptEnabled = true
        binding.wvAboutUs.webViewClient = WebViewClient()
        binding.wvAboutUs.webChromeClient = WebChromeClient()
        binding.wvAboutUs.loadUrl(url)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivDrawer -> {
                if (AppPrefs(requireContext()).getStringPref(ROLE).toString() == "Professional") {
                    (activity as ProfessionalActivity).isDrawer(true)
                } else {
                    (activity as CustomerActivity).isDrawer(true)
                }
            }
        }

    }
}