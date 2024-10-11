package com.app.gentlemanspa.ui.customerDashboard.fragment.editProfile.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.auth.fragment.register.model.ProfileRegisterResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.editProfile.model.UpdateProfileCustomerRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.editProfile.model.UpdateProfileCustomerResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model.ProfessionalDetail
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model.SpecialityResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model.UpdateProfileProfessionalRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model.UpdateProfileProfessionalResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateCustomerViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val firstName = ObservableField<String>()
    val lastName = ObservableField<String>()
    val phoneNumber = ObservableField<String>()
    val gender = ObservableField<String>()
    val id = ObservableField<String>()
    val email = ObservableField<String>()
    val specialityIds = ObservableField<String>()
    val professionalDetailId = ObservableField<String>()
    val profilePic = ObservableField<MultipartBody.Part>()
    val profileId = ObservableField<RequestBody>()
    val resultUpdateCustomerProfilePic= MutableLiveData<Resource<ProfileRegisterResponse>>()
    val resultUpdateCustomer = MutableLiveData<Resource<UpdateProfileCustomerResponse>>()

    fun updateCustomerProfile() {
        resultUpdateCustomer.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.updateCustomerDetail(
                UpdateProfileCustomerRequest(
                    id.get(),
                    firstName.get(),
                    lastName.get(),
                    gender.get(),
                    "+91",
                    phoneNumber.get(),
                    email.get(),
                    )
            )
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultUpdateCustomer.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }.collect {
                    if (it?.statusCode == 200) {
                        resultUpdateCustomer.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultUpdateCustomer.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun updateCustomerProfilePic() {
        resultUpdateCustomerProfilePic.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.profilePicRegister(profilePic.get(), profileId.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultUpdateCustomerProfilePic.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultUpdateCustomerProfilePic.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultUpdateCustomerProfilePic.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
}