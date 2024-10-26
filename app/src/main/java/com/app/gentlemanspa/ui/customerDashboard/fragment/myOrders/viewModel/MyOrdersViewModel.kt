package com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.CancelUpcomingAppointmentRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.CancelUpcomingAppointmentResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.model.MyOrdersResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MyOrdersViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val type = ObservableField<String>()

    val resultMyOrdersList = MutableLiveData<Resource<MyOrdersResponse>>()

    fun getMyOrders() {
        resultMyOrdersList.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getOrderedProducts(type.get()!!,
                1000,
                1,
            )
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultMyOrdersList.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultMyOrdersList.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultMyOrdersList.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

  /*  fun cancelUpcomingAppointment() {
        resultCancelUpcomingAppointment.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.cancelUpcomingAppointment(
                CancelUpcomingAppointmentRequest(orderId.get()!!,listOf(serviceBookingIds.get()!!))
            )
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultCancelUpcomingAppointment.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        //  Log.d("dataList","inside viewModel dataList size ${it.data.dataList.size} ")

                        resultCancelUpcomingAppointment.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultCancelUpcomingAppointment.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }*/
}