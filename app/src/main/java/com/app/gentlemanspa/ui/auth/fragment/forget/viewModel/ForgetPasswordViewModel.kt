package com.app.gentlemanspa.ui.auth.fragment.forget.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.auth.fragment.forget.model.ForgetPasswordRequest
import com.app.gentlemanspa.ui.auth.fragment.forget.model.ForgetPasswordResponse
import com.app.gentlemanspa.ui.auth.fragment.register.model.EmailOtpRequest
import com.app.gentlemanspa.ui.auth.fragment.register.model.EmailOtpResponse
import com.app.gentlemanspa.ui.auth.fragment.setPassword.model.ChangePasswordRequest
import com.app.gentlemanspa.ui.auth.fragment.setPassword.model.ChangePasswordResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ForgetPasswordViewModel(private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val email = ObservableField<String>()
    val resultEmailOtp = MutableLiveData<Resource<EmailOtpResponse>>()

    fun resetEmailOtp() {
        resultEmailOtp.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.emailOtp(EmailOtpRequest(email.get()!!, false))
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultEmailOtp.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultEmailOtp.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        if (it != null) {
                            resultEmailOtp.value =
                                Resource.error(data = null, message = it.messages)
                        }
                    }
                }
        }
    }
/*
    fun resetPassword() {
        resultForgetPassword.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.resetPassword(ForgetPasswordRequest(email.get(),newPassword.get()))
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultForgetPassword.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultForgetPassword.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultForgetPassword.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
*/

}