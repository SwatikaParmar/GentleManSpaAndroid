package com.app.gentlemanspa.ui.professionalDashboard.fragment.professionalRequests.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.professionalDashboard.fragment.professionalRequests.model.ProfessionalRequestsResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProfessionalRequestViewModel(private var initialRepository: InitialRepository) :
    AndroidViewModel(
        Application()
    ) {
    val professionalDetailId = ObservableField<Int>()
    val spaDetailId = ObservableField<Int>()
    val resultProfessionalRequests =
        MutableLiveData<Resource<ProfessionalRequestsResponse>>()

    fun getProfessionalRequestsApi() {
        resultProfessionalRequests.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProfessionalRequests(professionalDetailId.get()!!,spaDetailId.get()!!)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultProfessionalRequests.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultProfessionalRequests.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultProfessionalRequests.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}