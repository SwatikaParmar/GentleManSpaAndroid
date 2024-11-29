package com.app.gentlemanspa.ui.customerDashboard.fragment.product.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductCategoriesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsResponse
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

class ProductViewModel(private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val  mainCategoryId = ObservableField<Int>()
    val  productId = ObservableField<Int>()
    val  countInCart = ObservableField<Int>()
    val spaDetailId =ObservableField<Int>()
    val searchQuery =ObservableField<String>()
    val resultProductCategories = MutableLiveData<Resource<ProductCategoriesResponse>>()
    val resultProductsData= MutableLiveData<Resource<ProductsResponse>>()
    val resultGetCartItems = MutableLiveData<Resource<GetCartItemsResponse>>()
    val resultAddProductInCart = MutableLiveData<Resource<AddProductInCartResponse>>()



    fun getProductCategories() {
        resultProductCategories.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProductCategories()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultProductCategories.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultProductCategories.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultProductCategories.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultProductCategories.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }

                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultProductCategories.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultProductCategories.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun getProductsList() {
        resultProductsData.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProductsList(1,1000,mainCategoryId.get(),searchQuery.get()!!,spaDetailId.get()!!)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultProductsData.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultProductsData.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultProductsData.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultProductsData.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
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
    fun getProductCartItem() {
        Log.d("testIssue","inside getProductCartItem()")
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