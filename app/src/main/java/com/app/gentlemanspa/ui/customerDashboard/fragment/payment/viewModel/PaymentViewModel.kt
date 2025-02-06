package com.app.gentlemanspa.ui.customerDashboard.fragment.payment.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.model.CustomerPlaceOrderRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.model.CustomerPlaceOrderResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.payment.model.OrderConfirmationResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class PaymentViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val paymentId = ObservableField<Int>()
    val resultCustomerPlaceOrder = MutableLiveData<Resource<CustomerPlaceOrderResponse>>()
    val resultOrderConfirmation = MutableLiveData<Resource<OrderConfirmationResponse>>()



    fun orderConfirmationApi() {
        resultOrderConfirmation.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.orderConfirmation(paymentId.get()!!)
                .onStart { }
                .onCompletion { }
                .catch { exception ->

                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultOrderConfirmation.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultOrderConfirmation.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultOrderConfirmation.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultOrderConfirmation.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }

                .collect {
                    if (it?.status == true) {
                        resultOrderConfirmation.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultOrderConfirmation.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
    fun customerPlaceOrderApi(request: CustomerPlaceOrderRequest) {
        resultCustomerPlaceOrder.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.customerPlaceOrder(request)
                .onStart { }
                .onCompletion { }
                .catch { exception ->

                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultCustomerPlaceOrder.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultCustomerPlaceOrder.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultCustomerPlaceOrder.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultCustomerPlaceOrder.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }

                .collect {
                    if (it?.statusCode == 200) {
                        resultCustomerPlaceOrder.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultCustomerPlaceOrder.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }



}