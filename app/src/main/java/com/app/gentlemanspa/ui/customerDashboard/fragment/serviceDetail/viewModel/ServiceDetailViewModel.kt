package com.app.gentlemanspa.ui.customerDashboard.fragment.serviceDetail.viewModel

import android.app.Application
import android.database.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.CategoriesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.ServiceResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.serviceDetail.model.ServiceDetailResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ServiceDetailViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val serviceId = ObservableField<Int>()
    val spaDetailId = ObservableField<Int>()
    val resultServiceDetail = MutableLiveData<Resource<ServiceDetailResponse>>()




    fun getServiceDetail() {
        resultServiceDetail.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getServiceDetail(serviceId.get(),spaDetailId.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultServiceDetail.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultServiceDetail.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultServiceDetail.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}