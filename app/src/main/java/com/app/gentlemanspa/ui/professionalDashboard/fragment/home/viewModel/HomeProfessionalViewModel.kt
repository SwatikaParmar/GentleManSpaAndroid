package com.app.gentlemanspa.ui.professionalDashboard.fragment.home.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.model.GetProfessionalDetailResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeProfessionalViewModel(private var initialRepository: InitialRepository): AndroidViewModel(Application()) {

    val type = ObservableField<String>()
    val professionalDetailId = ObservableField<Int>()
    val resultProfileProfessionalDetailAccount = MutableLiveData<Resource<GetProfessionalDetailResponse>>()
    val resultAppointmentList = MutableLiveData<Resource<UpcomingServiceAppointmentResponse>>()


    fun getProfessionalDetail() {
        resultProfileProfessionalDetailAccount.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProfessionalDetail()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultProfileProfessionalDetailAccount.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultProfileProfessionalDetailAccount.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultProfileProfessionalDetailAccount.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun getAppointmentListApi() {
        resultAppointmentList.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getAppointmentsList(type.get()!!,
                1000,
                1,
                professionalDetailId.get()!!
            )
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultAppointmentList.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        Log.d("dataList","inside viewModel dataList size ${it.data.dataList.size} ")

                        resultAppointmentList.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultAppointmentList.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}

