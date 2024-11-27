package com.app.gentlemanspa.ui.customerDashboard.fragment.professionalTeamServices.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.professionalTeamServices.model.ProfessionalServicesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.AddServiceToCartRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.AddServiceToCartResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.GetCartItemsResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProfessionalServicesViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()

){
    val professionalDetailId = ObservableField<Int>()
    val spaDetailId =ObservableField<Int>()
    val serviceCountInCart =ObservableField<Int>()
    val slotId =ObservableField<Int>()
    val spaServiceId =ObservableField<Int>()
    val resultGetCartItems = MutableLiveData<Resource<GetCartItemsResponse>>()


    val resultProfessionalServices= MutableLiveData<Resource<ProfessionalServicesResponse>>()
    val resultServiceToCart = MutableLiveData<Resource<AddServiceToCartResponse>>()


    fun getProfessionalsServiceList() {
        resultProfessionalServices.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProfessionalServiceList(professionalDetailId.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultProfessionalServices.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultProfessionalServices.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultProfessionalServices.value =
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
}