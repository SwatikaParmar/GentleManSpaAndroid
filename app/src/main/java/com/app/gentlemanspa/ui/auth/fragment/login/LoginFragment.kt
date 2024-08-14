package com.app.gentlemanspa.ui.auth.fragment.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.gentlemanspa.ui.professionalDashboard.activity.ProfessionalActivity
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication.Companion.hideProgress
import com.app.gentlemanspa.base.MyApplication.Companion.showProgress
import com.app.gentlemanspa.databinding.FragmentLoginBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.auth.fragment.login.viewModel.LoginViewModel
import com.app.gentlemanspa.ui.customerDashboard.activity.CustomerActivity
import com.app.gentlemanspa.utils.AppPrefs
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.CommonFunctions.togglePasswordVisibility
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.checkString
import com.app.gentlemanspa.utils.showToast


class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentLoginBinding
    private var isPasswordVisible: Boolean = false
    private val viewModel: LoginViewModel by viewModels { ViewModelFactory(InitialRepository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initObserver() {
        viewModel.resultLoginAccount.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                      showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                          hideProgress()
                        if (it.data?.data?.role =="Customer" && it.data.data.passwordChanged ==null ){
                            val action = LoginFragmentDirections.actionLoginFragmentToSetPasswordFragment(it.data.data.email,binding.etActivationCode.text.toString())
                            findNavController().navigate(action)
                        }else if (it.data?.data?.role =="Customer" && it.data.data.passwordChanged ==true ){
                            AppPrefs(requireContext()).setString("TOKEN",it.data?.data?.token)
                            AppPrefs(requireContext()).setString("ROLE",it.data?.data?.role)
                            requireActivity().showToast(it.data.messages.toString())
                            startActivity(Intent(requireContext(), CustomerActivity::class.java))
                            requireActivity().finishAffinity()
                        }else if (it.data?.data?.role =="Professional"){
                            AppPrefs(requireContext()).setString("TOKEN",it.data?.data?.token)
                            AppPrefs(requireContext()).setString("ROLE",it.data?.data?.role)
                            AppPrefs(requireContext()).setString("CREATED_BY",it.data?.data?.id)
                            requireActivity().showToast(it.data.messages.toString())
                            startActivity(Intent(requireContext(), ProfessionalActivity::class.java))
                            requireActivity().finishAffinity()
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

    private fun initUI() {
        binding.onClick = this
        editTextSpace()
    }

    private fun editTextSpace() {
        CommonFunctions.startSpaceEditText(binding.etEmail)
        CommonFunctions.startSpaceEditText(binding.etPassword)
        CommonFunctions.startSpaceEditText(binding.etActivationCode)
    }


    override fun onClick(v: View?) {
        when (v) {
            binding.tvSignUp -> {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            binding.tvForgetPassword -> {
                findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
            }

            binding.btnSignIn -> {
                if (isValidation()) {
                    if (binding.etEmail.text.toString().isNotEmpty() && binding.etPassword.text.toString().isNotEmpty() && binding.etPassword.text.toString().isNotEmpty()){
                        viewModel.emailOrPhoneNumber.set(binding.etEmail.text.toString())
                        viewModel.password.set(binding.etPassword.text.toString())
                        viewModel.rememberMe.set(true)
                        viewModel.activationCode.set("")
                        viewModel.loginAccount()
                    }else if (binding.etEmail.text.toString().isNotEmpty() && binding.etPassword.text.toString().isNotEmpty()){
                        viewModel.emailOrPhoneNumber.set(binding.etEmail.text.toString())
                        viewModel.password.set(binding.etPassword.text.toString())
                        viewModel.rememberMe.set(true)
                        viewModel.activationCode.set("")
                        viewModel.loginAccount()

                    }else {
                        viewModel.emailOrPhoneNumber.set("")
                        viewModel.password.set("")
                        viewModel.rememberMe.set(true)
                        viewModel.activationCode.set(binding.etActivationCode.text.toString())
                        viewModel.loginAccount()
                    }

                }
            }


            binding.ivTogglePassword -> {
                isPasswordVisible = !isPasswordVisible
                togglePasswordVisibility(
                    binding.etPassword,
                    binding.ivTogglePassword,
                    isPasswordVisible
                )
            }


        }
    }


    private fun isValidation(): Boolean {
        when {

            (checkString(binding.etEmail)&&  checkString(binding.etActivationCode) )-> requireContext().showToast(
                "Please enter email or Activation Code" )

            (checkString(binding.etPassword) && (binding.etEmail.text.toString().isNotEmpty())) -> requireContext().showToast("Please enter password")

           /* ((binding.etEmail.text.toString().isNotEmpty()) && (binding.etPassword.text.toString().isNotEmpty()) && (binding.etActivationCode.text.toString().isNotEmpty())) ->{
                requireContext().showToast("Please login email and password or activation code")
            }*/


            else -> return true
        }

        return false
    }






}