package com.app.gentlemanspa.ui.customerDashboard.fragment.address.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.CustomerAddressResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.CustomerAddressStatusRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.CustomerAddressStatusResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.DeleteAddressResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AddressViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val spaDetailId = ObservableField<Int>()
    val serviceCountInCart = ObservableField<Int>()
    val slotId = ObservableField<Int>()
    val spaServiceId = ObservableField<Int>()
    val productId = ObservableField<Int>()
    val countInCart = ObservableField<Int>()
    val customerAddressId = ObservableField<Int>()
    val primaryAddressStatus = ObservableField<Boolean>()
    val resultCustomerAddress = MutableLiveData<Resource<CustomerAddressResponse>>()
    val resultCustomerAddressStatus = MutableLiveData<Resource<CustomerAddressStatusResponse>>()
    val resultCustomerDeleteAddress = MutableLiveData<Resource<DeleteAddressResponse>>()

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

    fun setCustomerAddressStatus() {
        resultCustomerAddressStatus.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.setCustomerAddressStatus(CustomerAddressStatusRequest( customerAddressId.get()!!,primaryAddressStatus.get()!!))
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
                        resultCustomerAddressStatus.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultCustomerAddressStatus.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
    fun deleteCustomerAddress() {
        resultCustomerDeleteAddress.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.deleteCustomerAddress(customerAddressId.get()!!)
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
                        resultCustomerDeleteAddress.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultCustomerDeleteAddress.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }



}