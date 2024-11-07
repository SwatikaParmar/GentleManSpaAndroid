package com.app.gentlemanspa.ui.auth.fragment.register.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.auth.fragment.register.model.EmailOtpRequest
import com.app.gentlemanspa.ui.auth.fragment.register.model.EmailOtpResponse
import com.app.gentlemanspa.ui.auth.fragment.register.model.PhoneUniqueRequest
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class RegisterViewModel(private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val email = ObservableField<String>()
    val phoneNumber = ObservableField<String>()
    val dialCode = ObservableField<String>()

    val resultEmailOtp = MutableLiveData<Resource<EmailOtpResponse>>()
    val resultPhoneUnique = MutableLiveData<Resource<EmailOtpResponse>>()


    fun emailOtp() {
        resultEmailOtp.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.emailOtp(EmailOtpRequest(email.get()!!, true))
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultEmailOtp.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultEmailOtp.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultEmailOtp.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultEmailOtp.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
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

    fun phoneUnique() {
        resultPhoneUnique.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.phoneUnique(PhoneUniqueRequest(phoneNumber.get()!!, dialCode.get()!!))
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultPhoneUnique.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it!!.statusCode == 200) {
                        resultPhoneUnique.value = Resource.success(message = it.messages, data = it)
                    } else {
                        resultPhoneUnique.value =
                            Resource.error(data = null, message = it.messages)
                    }
                }
        }
    }
    /*    fun registerAccount() {
            resultRegisterAccount.value = Resource.loading(null)
            viewModelScope.launch {
                initialRepository.registerAccount(
                    SignUpRequest(
                        firstName.get(),
                        lastName.get(),
                        password.get(),
                        phoneNumber.get(),
                        role.get(),
                        gender.get(),
                        email.get(),
                        dialCode.get()
                    )
                )
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
        }*/
}