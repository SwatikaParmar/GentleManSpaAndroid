package com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.model.ServiceGetAvailableDatesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.model.ServiceGetAvailableTimeSlotsResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.model.ServiceRescheduleRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.model.ServiceRescheduleResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.AddServiceToCartRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.AddServiceToCartResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.ServiceResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class MakeAppointmentViewModel(private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val spaServiceId = ObservableField<Int>()
    val professionalId = ObservableField<String>()
    val date = ObservableField<String>()
    val spaDetailId = ObservableField<Int>()
    val serviceCountInCart = ObservableField<Int>()
    val slotId = ObservableField<Int>()
    val orderId = ObservableField<Int>()
    val serviceBookingId = ObservableField<Int>()
    val resultServiceAvailableDates = MutableLiveData<Resource<ServiceGetAvailableDatesResponse>>()
    val resultServiceAvailableTimeSlots = MutableLiveData<Resource<ServiceGetAvailableTimeSlotsResponse>>()
    val resultAddServiceToCart = MutableLiveData<Resource<AddServiceToCartResponse>>()
    val resultServiceReschedule = MutableLiveData<Resource<ServiceRescheduleResponse>>()


    fun getServiceAvailableDates() {
        resultServiceAvailableDates.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getServiceAvailableDates(spaServiceId.get(), professionalId.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultServiceAvailableDates.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultServiceAvailableDates.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultServiceAvailableDates.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun getServiceAvailableTimeSlots() {
        resultServiceAvailableTimeSlots.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getServiceAvailableTimeSlots(
                spaServiceId.get(),
                date.get()!!,
                professionalId.get()
            )
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultServiceAvailableTimeSlots.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultServiceAvailableTimeSlots.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultServiceAvailableTimeSlots.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun addServiceToCart() {
        resultAddServiceToCart.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.addServiceToCart(
                AddServiceToCartRequest(
                    serviceCountInCart.get()!!,
                    slotId.get()!!,
                    spaDetailId.get()!!,
                    spaServiceId.get()!!))
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage =
                                    jsonError.optString("messages", "Unknown HTTP error")
                                resultAddServiceToCart.value =
                                    Resource.error(data = null, message = errorMessage)
                            } else {
                                resultAddServiceToCart.value =
                                    Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultAddServiceToCart.value =
                                Resource.error(data = null, message = e.message) }
                    } else {
                        resultAddServiceToCart.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultAddServiceToCart.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultAddServiceToCart.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun serviceReschedule() {
        resultServiceReschedule.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.serviceReschedule(
                ServiceRescheduleRequest(
                    orderId.get()!!,
                    serviceBookingId.get()!!,
                    slotId.get()!!
                )
            )
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage =
                                    jsonError.optString("messages", "Unknown HTTP error")
                                resultServiceReschedule.value =
                                    Resource.error(data = null, message = errorMessage)
                            } else {
                                resultServiceReschedule.value =
                                    Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultServiceReschedule.value =
                                Resource.error(data = null, message = e.message)
                        }
                    } else {
                        resultServiceReschedule.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultServiceReschedule.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultServiceReschedule.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}