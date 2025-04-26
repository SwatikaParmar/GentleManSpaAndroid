package com.app.gentlemanspa.ui.customerDashboard.fragment.history.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.AddUserToChatRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.AddUserToChatResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.CancelUpcomingAppointmentRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.CancelUpcomingAppointmentResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class HistoryViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val userId = ObservableField<String>()
    val type = ObservableField<String>()
    val orderId = ObservableField<Int>()
    val productOrderIds  = ObservableField<Int>()
    val serviceBookingIds  = ObservableField<Int>()

    val resultUpcomingServiceAppointmentList = MutableLiveData<Resource<UpcomingServiceAppointmentResponse>>()
    val resultCancelUpcomingAppointment = MutableLiveData<Resource<CancelUpcomingAppointmentResponse>>()
    val resultAddUserToChat= MutableLiveData<Resource<AddUserToChatResponse>>()


    fun getServiceAppointments() {
        resultUpcomingServiceAppointmentList.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getServiceAppointments(userId.get()!!,type.get()!!,
                1000,
                1,
            )
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        when (exception.code()) {
                            401 -> {
                                resultUpcomingServiceAppointmentList.value = Resource.error(
                                    data = null,
                                    message = "${exception.code()}"
                                )
                                return@catch
                            }

                            else -> {
                                try {
                                    val errorBody = exception.response()?.errorBody()?.string()
                                    if (!errorBody.isNullOrEmpty()) {
                                        val jsonError = JSONObject(errorBody)
                                        val errorMessage =
                                            jsonError.optString("messages", "Unknown HTTP error")
                                        resultUpcomingServiceAppointmentList.value =
                                            Resource.error(data = null, message = errorMessage)
                                    } else {
                                        resultUpcomingServiceAppointmentList.value =
                                            Resource.error(
                                                data = null,
                                                message = "Unknown HTTP error"
                                            )
                                    }
                                } catch (e: Exception) {
                                    resultUpcomingServiceAppointmentList.value =
                                        Resource.error(data = null, message = e.message)
                                }

                            }
                        }

                    } else {
                        resultUpcomingServiceAppointmentList.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        Log.d("dataList","inside viewModel dataList size ${it.data.dataList.size} ")

                        resultUpcomingServiceAppointmentList.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultUpcomingServiceAppointmentList.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun cancelUpcomingAppointment() {
        resultCancelUpcomingAppointment.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.cancelUpcomingAppointment(
                CancelUpcomingAppointmentRequest(orderId.get()!!,listOf(serviceBookingIds.get()!!)))
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
    }

    fun addUserToChatApi(addUserToChatRequest: AddUserToChatRequest) {
        resultAddUserToChat.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.addUserToChat(addUserToChatRequest)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultAddUserToChat.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        //  Log.d("dataList","inside viewModel dataList size ${it.data.dataList.size} ")

                        resultAddUserToChat.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultAddUserToChat.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}