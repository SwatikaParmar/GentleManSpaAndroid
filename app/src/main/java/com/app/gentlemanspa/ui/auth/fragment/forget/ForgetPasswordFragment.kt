package com.app.gentlemanspa.ui.auth.fragment.forget

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentForgetPasswordBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.auth.fragment.forget.viewModel.ForgetPasswordViewModel
import com.app.gentlemanspa.ui.auth.fragment.otp.model.SignUpRequest
import com.app.gentlemanspa.ui.auth.fragment.setPassword.viewModel.SetPasswordViewModel
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.checkString
import com.app.gentlemanspa.utils.checkValidString
import com.app.gentlemanspa.utils.showToast


class ForgetPasswordFragment : Fragment(), View.OnClickListener {

    private var isPasswordVisible: Boolean = false
    private var isConfirmPasswordVisible: Boolean = false
    private lateinit var binding: FragmentForgetPasswordBinding
    private val viewModel: ForgetPasswordViewModel by viewModels { ViewModelFactory(InitialRepository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
    private fun initObserver() {
        viewModel.resultSendEmailOtp.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        if (it.data!!.isSuccess){
                            Log.d("forgetPassword","OTP ->${it.data.data.otp}")
                            requireContext().showToast(it.data.messages)
                            val signUpRequest= SignUpRequest("","",binding.etPassword.text.toString().trim(),"","","",binding.etEmail.text.toString().trim(),"")
                            val action = ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToOtpFragment( it.data.data.otp.toString(),"", signUpRequest,1)
                            findNavController().navigate(action)
                        }else{
                            requireContext().showToast(it.data.messages.toString())
                        }
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
            binding.ivBack->{
                findNavController().popBackStack()
            }
            binding.ivTogglePassword -> {
                isPasswordVisible = !isPasswordVisible
                CommonFunctions.togglePasswordVisibility(
                    binding.etPassword,
                    binding.ivTogglePassword,
                    isPasswordVisible
                )
            }

            binding.ivToggleConfirmPassword -> {
                isConfirmPasswordVisible = !isConfirmPasswordVisible
                CommonFunctions.togglePasswordVisibility(
                    binding.etConfirmPassword,
                    binding.ivToggleConfirmPassword,
                    isConfirmPasswordVisible
                )
            }
            binding.btnOtp -> {
                if (isValidation()) {
                    viewModel.email.set(binding.etEmail.text.toString())
                    viewModel.resetEmailOtp()

                }

            }

        }
    }
    private fun isValidation(): Boolean {
        when {
            checkString(binding.etEmail) -> requireContext().showToast("Please enter email")
            checkString(binding.etPassword) -> requireContext().showToast("Please enter new password")
            checkValidString(binding.etPassword).length < 6 -> requireContext().showToast("Password should be 6 characters or more")
            checkString(binding.etConfirmPassword) -> requireContext().showToast("Please enter confirm password")
            (checkValidString(binding.etPassword) != checkValidString(binding.etConfirmPassword)) -> requireContext().showToast("Password and confirm password mismatched")
            else -> return true
        }

        return false
    }

}