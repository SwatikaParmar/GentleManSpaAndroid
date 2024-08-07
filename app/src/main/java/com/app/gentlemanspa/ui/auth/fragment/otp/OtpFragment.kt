package com.app.gentlemanspa.ui.auth.fragment.otp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.databinding.AccountCreatedDialogBinding
import com.app.gentlemanspa.databinding.FragmentOtpBinding
import com.app.gentlemanspa.utils.showToast


class OtpFragment : Fragment(), View.OnClickListener {

    private lateinit var binding : FragmentOtpBinding
    val args : OtpFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOtpBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.onClick = this
    }


    private fun showAccountRegister() {
        val dialog = Dialog(requireContext())
        val bindingDialog = AccountCreatedDialogBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(bindingDialog.root)
        dialog.setCancelable(false)

        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        bindingDialog.btnContinue.setOnClickListener {
            startActivity(Intent(requireContext(), ProfessionalActivity::class.java))
            requireActivity().finish()
            dialog.dismiss()
        }


    }

    override fun onClick(v: View?) {
        when(v) {
            binding.btnVerify -> {
                if (args.OtpType ==1){
                    requireActivity().showToast("Coming soon")
                }else{
                    showAccountRegister()
                }

            }
        }
    }


}