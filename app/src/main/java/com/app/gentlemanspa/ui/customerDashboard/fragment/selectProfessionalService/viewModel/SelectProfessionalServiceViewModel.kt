package com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessionalService.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.AddServiceToCartResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.GetCartItemsResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SelectProfessionalServiceViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val resultGetCartItems = MutableLiveData<Resource<GetCartItemsResponse>>()

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