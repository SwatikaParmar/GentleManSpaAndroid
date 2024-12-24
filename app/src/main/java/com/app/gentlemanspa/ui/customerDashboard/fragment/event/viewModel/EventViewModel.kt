package com.app.gentlemanspa.ui.customerDashboard.fragment.event.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.event.model.EventListResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class EventViewModel  (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val type = ObservableField<String>()
    val orderId = ObservableField<Int>()
    val productOrderIds  = ObservableField<Int>()
    val serviceBookingIds  = ObservableField<Int>()
    val resultEventList = MutableLiveData<Resource<EventListResponse>>()


    fun getEventListApi() {
        resultEventList.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getEventList()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultEventList.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultEventList.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultEventList.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultEventList.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultEventList.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultEventList.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}