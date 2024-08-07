package com.app.gentlemanspa.ui.auth.fragment.forget

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.databinding.FragmentForgetPasswordBinding


class ForgetPasswordFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentForgetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgetPasswordBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.onClick = this
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.btnOtp -> {
                val action = ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToOtpFragment(1)
                findNavController().navigate(action)
            }

        }
    }


}