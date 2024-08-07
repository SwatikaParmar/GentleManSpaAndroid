package com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.auth.fragment.register.model.ProfileRegisterResponse
import com.app.gentlemanspa.ui.auth.fragment.register.model.SignUpResponse
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

class UpdateProfessionalViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val firstName  = ObservableField<String>()
    val lastName  = ObservableField<String>()
    val phoneNumber = ObservableField<String>()
    val gender = ObservableField<String>()
    val id = ObservableField<String>()
    val email = ObservableField<String>()

    val pincode = ObservableField<String>()
    val country = ObservableField<String>()
    val award = ObservableField<String>()
    val city = ObservableField<String>()
    val streetAddress = ObservableField<String>()
    val houseNoOrBuildingName = ObservableField<String>()
    val state = ObservableField<String>()
    val trainingLevel = ObservableField<String>()
    val status = ObservableField<String>()
    val specialityIds = ObservableField<String>()

    val profilePic = ObservableField<MultipartBody.Part>()
    val profileId = ObservableField<RequestBody>()



    val resultProfileRegister = MutableLiveData<Resource<ProfileRegisterResponse>>()
    val resultSpeciality = MutableLiveData<Resource<SpecialityResponse>>()

    val resultUpdateProfessional = MutableLiveData<Resource<UpdateProfileProfessionalResponse>>()



    fun updateProfessional() {
        resultUpdateProfessional.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.updateProfessional(UpdateProfileProfessionalRequest(firstName.get(),lastName.get(),phoneNumber.get(),gender.get(),ProfessionalDetail(pincode.get(),country.get(),award.get(),city.get(),streetAddress.get(),houseNoOrBuildingName.get(),state.get(), trainingLevel.get(),status.get(), specialityIds.get()),id.get(),email.get()))
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultUpdateProfessional.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultUpdateProfessional.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultUpdateProfessional.value =
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


    fun getSpeciality() {
        resultSpeciality.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getSpeciality()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultSpeciality.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultSpeciality.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultSpeciality.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}