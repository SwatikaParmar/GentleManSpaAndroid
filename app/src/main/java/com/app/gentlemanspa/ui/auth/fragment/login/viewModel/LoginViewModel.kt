package com.app.gentlemanspa.ui.auth.fragment.login.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.auth.fragment.login.model.LoginRequest
import com.app.gentlemanspa.ui.auth.fragment.login.model.LoginResponse
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

class LoginViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {



    val password  = ObservableField<String>()
    val emailOrPhoneNumber  = ObservableField<String>()
    val rememberMe  = ObservableField<Boolean>()
    val activationCode  = ObservableField<String>()

    val resultLoginAccount = MutableLiveData<Resource<LoginResponse>>()



    fun loginAccount() {
        resultLoginAccount.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.loginAccount(LoginRequest(password.get(),emailOrPhoneNumber.get(),rememberMe.get(),activationCode.get()))
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultLoginAccount.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultLoginAccount.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultLoginAccount.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}