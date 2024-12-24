package com.app.gentlemanspa.ui.customerDashboard.fragment.orderDetail.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.model.MyOrdersResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.orderDetail.model.OrderDetailsResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class OrderDetailsViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val orderId = ObservableField<Int>()
    val resultOrderDetail= MutableLiveData<Resource<OrderDetailsResponse>>()

    fun getOrderDetailsApi() {
        resultOrderDetail.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getOrderDetails(orderId.get()!!,
            )
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultOrderDetail.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultOrderDetail.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultOrderDetail.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultOrderDetail.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultOrderDetail.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultOrderDetail.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }


}