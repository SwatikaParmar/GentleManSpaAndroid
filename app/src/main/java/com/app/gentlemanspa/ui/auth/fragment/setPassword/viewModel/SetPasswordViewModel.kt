package com.app.gentlemanspa.ui.auth.fragment.setPassword.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.auth.fragment.login.model.LoginRequest
import com.app.gentlemanspa.ui.auth.fragment.login.model.LoginResponse
import com.app.gentlemanspa.ui.auth.fragment.setPassword.model.ChangePasswordRequest
import com.app.gentlemanspa.ui.auth.fragment.setPassword.model.ChangePasswordResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SetPasswordViewModel(private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val currentPassword  = ObservableField<String>()
    val newPassword  = ObservableField<String>()
    val email  = ObservableField<String>()
    val activationCode  = ObservableField<String>()
    val resultChangePassword = MutableLiveData<Resource<ChangePasswordResponse>>()



    fun setChangePassword() {
        resultChangePassword.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.setChangePassword(ChangePasswordRequest(newPassword.get(),activationCode.get(),email.get(),currentPassword.get()))
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultChangePassword.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultChangePassword.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultChangePassword.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}