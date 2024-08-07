package com.app.gentlemanspa.ui.auth.fragment.register.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.auth.fragment.register.model.ProfileRegisterResponse
import com.app.gentlemanspa.ui.auth.fragment.register.model.SignUpRequest
import com.app.gentlemanspa.ui.auth.fragment.register.model.SignUpResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RegisterViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {



    val firstName  = ObservableField<String>()
    val lastName  = ObservableField<String>()
    val email  = ObservableField<String>()
    val phoneNumber  = ObservableField<String>()
    val password  = ObservableField<String>()
    val dialCode  = ObservableField<String>()
    val role  = ObservableField<String>()
    val gender  = ObservableField<String>()

    val profilePic = ObservableField<MultipartBody.Part>()
    val profileId = ObservableField<RequestBody>()



    //val resultEmailOtp = MutableLiveData<Resource<EmailOtpResponse>>()
    val resultRegisterAccount = MutableLiveData<Resource<SignUpResponse>>()
    val resultProfileRegister = MutableLiveData<Resource<ProfileRegisterResponse>>()



   /* fun emailOtp() {
        resultEmailOtp.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.emailOtp(EmailOtpRequest(isUserExisting.get(),email.get()))
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
                    if (it?.code == 200) {
                        resultEmailOtp.value =
                            Resource.success(message = it.message, data = it)
                    } else {
                        resultEmailOtp.value =
                            Resource.error(data = null, message = it?.message)
                    }
                }
        }
    }*/






    fun registerAccount() {
        resultRegisterAccount.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.registerAccount(SignUpRequest(firstName.get(),lastName.get(),password.get(),phoneNumber.get(),role.get(),gender.get(),email.get(),dialCode.get()))
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
}