package com.app.gentlemanspa.ui.customerDashboard.fragment.service.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.CategoriesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.AddServiceToCartRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.AddServiceToCartResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.GetCartItemsResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.ServiceResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ServiceViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val categoryId =ObservableField<Int>()
    val spaDetailId =ObservableField<Int>()
    val serviceCountInCart =ObservableField<Int>()
    val slotId =ObservableField<Int>()
    val spaServiceId =ObservableField<Int>()
    val searchQuery =ObservableField<String>()
    val resultCategories = MutableLiveData<Resource<CategoriesResponse>>()
    val resultServiceList = MutableLiveData<Resource<ServiceResponse>>()
    val resultGetCartItems = MutableLiveData<Resource<GetCartItemsResponse>>()
    val resultServiceToCart = MutableLiveData<Resource<AddServiceToCartResponse>>()


    fun getCategories() {
        resultCategories.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getCategories()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultCategories.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultCategories.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultCategories.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun getServiceList() {
        resultServiceList.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getServiceList(1,1000,categoryId.get(),searchQuery.get()!!,spaDetailId.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultServiceList.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultServiceList.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultServiceList.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
    fun getServiceCartItem() {
        resultGetCartItems.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getServiceCartItems()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultGetCartItems.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
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

}