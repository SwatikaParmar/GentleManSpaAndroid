package com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement.model.AddUpdateRequestToManagementRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.requestToManagement.model.AddUpdateRequestToManagementResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RequestToManagementViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val resultAddUpdateRequestToManagement = MutableLiveData<Resource<AddUpdateRequestToManagementResponse>>()
    fun addUpdateProfessionalRequestToManagement(request: AddUpdateRequestToManagementRequest) {
        resultAddUpdateRequestToManagement.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.addUpdateRequestToManagement(request)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultAddUpdateRequestToManagement.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultAddUpdateRequestToManagement.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultAddUpdateRequestToManagement.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}