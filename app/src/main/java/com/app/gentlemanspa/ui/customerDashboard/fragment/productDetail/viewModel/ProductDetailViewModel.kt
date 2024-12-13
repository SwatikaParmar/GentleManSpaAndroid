package com.app.gentlemanspa.ui.customerDashboard.fragment.productDetail.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.model.AddProductInCartRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.model.AddProductInCartResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.productDetail.model.ProductDetailResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProductDetailViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val id = ObservableField<Int>()
    val productId = ObservableField<Int>()
    val countInCart = ObservableField<Int>()
    val resultProductDetail= MutableLiveData<Resource<ProductDetailResponse>>()
    val resultAddProductInCart = MutableLiveData<Resource<AddProductInCartResponse>>()


    fun getProductDetails() {
        resultProductDetail.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProductDetails(id.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultProductDetail.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultProductDetail.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultProductDetail.value =
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


}