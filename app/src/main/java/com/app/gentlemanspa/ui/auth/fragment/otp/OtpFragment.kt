package com.app.gentlemanspa.ui.auth.fragment.otp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.databinding.AccountCreatedDialogBinding
import com.app.gentlemanspa.databinding.FragmentOtpBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.auth.fragment.forget.ForgetPasswordFragmentDirections
import com.app.gentlemanspa.ui.auth.fragment.otp.model.SignUpRequest
import com.app.gentlemanspa.ui.auth.fragment.otp.viewModel.OtpViewModel
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CommonFunctions.getTextRequestBodyParams
import com.app.gentlemanspa.utils.CommonFunctions.prepareFilePart
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.showToast
import java.io.File


@Suppress("DEPRECATION")
class OtpFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentOtpBinding
    private var signUpRequest: SignUpRequest? = null
    private val args: OtpFragmentArgs by navArgs()
    private var messagesRegister: String? = ""
    private var profileImage: File? = null
    var otp=""
    private val viewModel: OtpViewModel by viewModels { ViewModelFactory(InitialRepository()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOtpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        getData()
        initUI()
    }

    private fun getData() {
        otp=args.Otp
    }

    private fun initObserver() {

        viewModel.resultRegisterAccount.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        //  hideProgress()
                        messagesRegister = it.data?.messages.toString()
                        AppPrefs(requireContext()).setString("TOKEN", it.data?.data?.token)
                        Log.d("signUpRequest", "register currentPhotoPath->${args.currentPhotoPath}")

                        if (args.currentPhotoPath != "null" ) {
                            Log.d("signUpRequest", "inside currentPhotoPath")
                            profileImage = File(args.currentPhotoPath)
                            viewModel.profileId.set(getTextRequestBodyParams(it.data?.data?.id))
                            viewModel.profilePic.set(prepareFilePart("profilePic", profileImage!!))
                            viewModel.profilePicRegister()
                        } else {
                            Log.d("signUpRequest", "inside else part currentPhotoPath")
                            hideProgress()
                            requireContext().showToast(messagesRegister.toString())
                            showAccountRegister()
                        }

                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultProfileRegister.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        requireContext().showToast(messagesRegister.toString())
                        showAccountRegister()

                    }

                    Status.ERROR -> {
                        hideProgress()
                        if (it.message != null) {
                            requireContext().showToast(it.message.toString())
                        }
                    }
                }
            }
        }

        viewModel.resultForgetPassword.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        if (it.data!!.isSuccess!!) {
                            requireContext().showToast(it.data.messages.toString())
                            val action = OtpFragmentDirections.actionOtpFragmentToLoginFragment()
                            findNavController().navigate(action)

                        } else {
                            requireContext().showToast(it.data.messages.toString())
                        }
                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }
        viewModel.resultResendEmailOtp.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        hideProgress()
                        requireContext().showToast(it.data?.messages.toString())
                        if (it.data?.data?.otp.toString().isNotEmpty()){
                            otp=it.data?.data?.otp.toString()
                        }


                    }

                    Status.ERROR -> {
                        requireContext().showToast(it.message.toString())
                        hideProgress()
                    }
                }
            }
        }


    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        binding.onClick = this
        arguments?.let {
            signUpRequest = it.getParcelable("signUpRequest")
        }
        Log.d("signUpRequest", "signUpRequest->${signUpRequest!!.email}")
        binding.tvEnterCode.text = "${getString(R.string.please_enter_5_digit_code_sent_to_email)} ${signUpRequest!!.email}"
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
        when (v) {
            binding.ivArrowBack  ->{
                findNavController().popBackStack()
            }

            binding.btnVerify -> {
                Log.d("signUpRequest", "Otp->${otp}")
                if (args.OtpType == 1) {
                    resetPassword()
                } else {
                    registerUser()
                }

            }
            binding.tvResendCode->{
                 proceedToResendCode()
            }
        }
    }

    private fun proceedToResendCode() {
        binding.pvOtp.text?.clear()
        Log.d("proceedToResendCode", "proceedToResendCode email->${signUpRequest!!.email}")
        if (args.OtpType == 1) {
            viewModel.isVerify.set(false)
        }else{
            viewModel.isVerify.set(true)
        }
        viewModel.email.set(signUpRequest!!.email)
        viewModel.resendEmailOtp()
    }

    private
    fun resetPassword() {
        if (binding.pvOtp.text!!.isEmpty()) {
            requireActivity().showToast("Please Enter OTP")
        } else {
            if (binding.pvOtp.text.toString() == args.Otp) {
                Log.d("signUpRequest", "signUpRequest->${signUpRequest!!}")
                viewModel.email.set(signUpRequest!!.email)
                viewModel.newPassword.set(signUpRequest!!.password)
                viewModel.resetPassword()
            } else {
                requireActivity().showToast(" Enter Valid  OTP")
            }

        }

    }

    private fun registerUser() {
        if (binding.pvOtp.text!!.isEmpty()) {
            requireActivity().showToast("Please Enter OTP")
        } else {
            if (binding.pvOtp.text.toString() == otp) {
                if (signUpRequest != null) {
                    viewModel.registerAccount(signUpRequest!!)
                }
            } else {
                requireActivity().showToast(" Enter Valid  OTP")
            }
        }
    }


}