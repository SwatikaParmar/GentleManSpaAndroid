package com.app.gentlemanspa.ui.auth.fragment.setPassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.gentlemanspa.R
import com.app.gentlemanspa.base.MyApplication
import com.app.gentlemanspa.databinding.FragmentSetPasswordBinding
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.network.Status
import com.app.gentlemanspa.ui.auth.fragment.setPassword.viewModel.SetPasswordViewModel
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.ViewModelFactory
import com.app.gentlemanspa.utils.checkString
import com.app.gentlemanspa.utils.checkValidString
import com.app.gentlemanspa.utils.showToast


class SetPasswordFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentSetPasswordBinding
    private var isPasswordVisible: Boolean = false
    private var isConfirmPasswordVisible: Boolean = false
    val args : SetPasswordFragmentArgs by navArgs()

    private val viewModel: SetPasswordViewModel by viewModels { ViewModelFactory(InitialRepository()) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initUI()
    }

    private fun initUI() {
        binding.etEmail.setText(args.Email)
        binding.onClick=this
    }

    private fun initObserver() {
        viewModel.resultChangePassword.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    Status.LOADING -> {
                        MyApplication.showProgress(requireContext())
                    }

                    Status.SUCCESS -> {
                        MyApplication.hideProgress()
                        requireContext().showToast(it.data?.messages.toString())
                        findNavController().navigate(R.id.loginFragment)

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

            binding.btnContinue -> {
                if (isValidation()) {
                    viewModel.email.set(args.Email)
                    viewModel.activationCode.set(args.ActivationCode)
                    viewModel.newPassword.set(binding.etPassword.text.toString())
                    viewModel.currentPassword.set(binding.etPassword.text.toString())
                    viewModel.setChangePassword()

                }
            }

            binding.ivBack -> {
                findNavController().popBackStack()
            }



        }
    }

    private fun isValidation(): Boolean {
        when {

            checkString(binding.etPassword) -> requireContext().showToast("Please enter new password")

            checkValidString(binding.etPassword).length < 6 -> requireContext().showToast("Password should be 6 characters or more")

            checkString(binding.etConfirmPassword) -> requireContext().showToast("Please enter confirm password")

            (checkValidString(binding.etPassword) != checkValidString(binding.etConfirmPassword)) -> requireContext().showToast("Password and confirm password mismatched")


            else -> return true
        }

        return false
    }
}