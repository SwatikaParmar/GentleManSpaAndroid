package com.app.gentlemanspa.ui.auth.fragment.otp.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.auth.fragment.forget.model.ForgetPasswordRequest
import com.app.gentlemanspa.ui.auth.fragment.forget.model.ForgetPasswordResponse
import com.app.gentlemanspa.ui.auth.fragment.register.model.ProfileRegisterResponse
import com.app.gentlemanspa.ui.auth.fragment.otp.model.SignUpRequest
import com.app.gentlemanspa.ui.auth.fragment.otp.model.SignUpResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class OtpViewModel(private var initialRepository: InitialRepository):AndroidViewModel(Application()) {
    val profilePic = ObservableField<MultipartBody.Part>()
    val profileId = ObservableField<RequestBody>()
    val newPassword  = ObservableField<String>()
    val email  = ObservableField<String>()
    val resultForgetPassword = MutableLiveData<Resource<ForgetPasswordResponse>>()
    val resultRegisterAccount = MutableLiveData<Resource<SignUpResponse>>()
    val resultProfileRegister = MutableLiveData<Resource<ProfileRegisterResponse>>()

    fun registerAccount(signUpRequest:SignUpRequest) {
        resultRegisterAccount.value = Resource.loading(null)
        viewModelScope.launch {

            initialRepository.registerAccount(signUpRequest)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultRegisterAccount.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultRegisterAccount.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultRegisterAccount.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun profilePicRegister() {
        resultProfileRegister.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.profilePicRegister(profilePic.get(), profileId.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultProfileRegister.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultProfileRegister.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultProfileRegister.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }


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

}