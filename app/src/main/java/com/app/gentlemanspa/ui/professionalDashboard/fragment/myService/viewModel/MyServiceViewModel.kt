package com.app.gentlemanspa.ui.professionalDashboard.fragment.myService.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.CategoriesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.ServiceResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.myService.model.ProfessionalServiceResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MyServiceViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val professionalDetailId = ObservableField<Int>()
    val resultProfessionalServiceList = MutableLiveData<Resource<ProfessionalServiceResponse>>()

    fun getProfessionalServiceList() {
        resultProfessionalServiceList.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProfessionalServiceList(1,1000,professionalDetailId.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultProfessionalServiceList.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultProfessionalServiceList.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultProfessionalServiceList.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
}