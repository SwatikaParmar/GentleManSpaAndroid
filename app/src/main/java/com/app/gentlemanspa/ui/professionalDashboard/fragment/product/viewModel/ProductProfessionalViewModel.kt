package com.app.gentlemanspa.ui.professionalDashboard.fragment.product.viewModel

import android.app.Application
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductCategoriesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.product.model.DeleteProductResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProductProfessionalViewModel(private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val  mainCategoryId = ObservableField<Int>()
    val  id = ObservableField<Int>()
    val resultProductsData= MutableLiveData<Resource<ProductsResponse>>()
    val resultDeleteProduct = MutableLiveData<Resource<DeleteProductResponse>>()





    fun getProductsList() {
        resultProductsData.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProductsList(1,1000,mainCategoryId.get(),"",21)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultProductsData.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultProductsData.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultProductsData.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun getDeleteProduct() {
        resultDeleteProduct.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getDeleteProduct(id.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultDeleteProduct.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultDeleteProduct.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultDeleteProduct.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}