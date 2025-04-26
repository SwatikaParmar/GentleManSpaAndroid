package com.app.gentlemanspa.ui.customerDashboard.fragment.cart.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.CustomerAddressResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.model.CustomerPlaceOrderRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.model.CustomerPlaceOrderResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.model.PayByStripeRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.model.PayByStripeResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.model.AddProductInCartRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.model.AddProductInCartResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.AddServiceToCartRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.AddServiceToCartResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.GetCartItemsResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class CartViewModel(private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val spaDetailId = ObservableField<Int>()
    val serviceCountInCart = ObservableField<Int>()
    val slotId = ObservableField<Int>()
    val spaServiceId = ObservableField<Int>()
    val productId = ObservableField<Int>()
    val countInCart = ObservableField<Int>()
    val customerAddressId = ObservableField<Int>()
    val paymentId = ObservableField<Int>()
    val deliveryType = ObservableField<String>()
    val paymentType = ObservableField<String>()
    val resultGetCartItems = MutableLiveData<Resource<GetCartItemsResponse>>()
    val resultServiceToCart = MutableLiveData<Resource<AddServiceToCartResponse>>()
    val resultAddProductInCart = MutableLiveData<Resource<AddProductInCartResponse>>()
    val resultCustomerAddress = MutableLiveData<Resource<CustomerAddressResponse>>()
    val resultCustomerPlaceOrder = MutableLiveData<Resource<CustomerPlaceOrderResponse>>()
    val resultPayByStripe = MutableLiveData<Resource<PayByStripeResponse>>()


    fun getCartItem() {
        resultGetCartItems.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getServiceCartItems()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        when (exception.code()) {
                            401 -> {
                                resultGetCartItems.value = Resource.error(
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
                                        resultGetCartItems.value =
                                            Resource.error(data = null, message = errorMessage)
                                    } else {
                                        resultGetCartItems.value =
                                            Resource.error(
                                                data = null,
                                                message = "Unknown HTTP error"
                                            )
                                    }
                                } catch (e: Exception) {
                                    resultGetCartItems.value =
                                        Resource.error(data = null, message = e.message)
                                }

                            }
                        }

                    } else {
                        resultGetCartItems.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultGetCartItems.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultGetCartItems.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
    fun addServiceToCart() {
        resultServiceToCart.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.addServiceToCart(AddServiceToCartRequest(serviceCountInCart.get()!!, slotId.get()!!,spaDetailId.get()!!,spaServiceId.get()!!))
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultServiceToCart.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultServiceToCart.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultServiceToCart.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
    fun addProductInCart() {
        resultAddProductInCart.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.addProductInCart(AddProductInCartRequest(countInCart.get()!!, productId.get()!!))
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultAddProductInCart.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultAddProductInCart.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultAddProductInCart.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun geCustomerAddressList() {
        resultCustomerAddress.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.geCustomerAddressList()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultCustomerAddress.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultCustomerAddress.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultCustomerAddress.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun customerPlaceOrder() {
        resultCustomerPlaceOrder.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.customerPlaceOrder(CustomerPlaceOrderRequest(customerAddressId.get()!!, deliveryType.get()!!,paymentType.get()!!,paymentId.get()!!))
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

    fun payByStripeApi(body: PayByStripeRequest) {
        resultPayByStripe.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.payByStripe(body)
                .onStart { }
                .onCompletion { }
                .catch { exception ->

                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultPayByStripe.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultPayByStripe.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultPayByStripe.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultPayByStripe.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }

                .collect {
                    if (it?.status == true) {
                        resultPayByStripe.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultPayByStripe.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}