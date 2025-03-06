package com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductCategoriesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.productDetail.model.ProductDetailResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.AddProductRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.AddProductResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.UpdateProductRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.UpdateProductResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.UploadProductImageResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException

class AddProductViewModel(private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val listingPrice = ObservableField<Double>()
    val createdBy = ObservableField<String>()
    val name = ObservableField<String>()
    val description = ObservableField<String>()
    val subCategoryId = ObservableField<Int>()
    val spaDetailId = ObservableField<Int>()
    val mainCategoryId = ObservableField<Int>()
    val basePrice = ObservableField<Double>()
    val stock = ObservableField<Int>()
    val quantity = ObservableField<Int>()
    val productUpdateId = ObservableField<Int>()
    val productId = ObservableField<RequestBody>()
    val productImages = ObservableField<ArrayList<MultipartBody.Part>>()
    val id = ObservableField<Int>()
    val resultProductCategories = MutableLiveData<Resource<ProductCategoriesResponse>>()
    val resultAddProduct = MutableLiveData<Resource<AddProductResponse>>()
    val resultUpdateProduct = MutableLiveData<Resource<UpdateProductResponse>>()
    val resultUploadProductImage = MutableLiveData<Resource<UploadProductImageResponse>>()
    val resultProductDetail = MutableLiveData<Resource<ProductDetailResponse>>()

    fun getProductCategories() {
        resultProductCategories.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProductCategories()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    /* if (!CommonFunctions.getError(exception)!!.contains("401"))
                         resultProductCategories.value =
                             Resource.error(
                                 data = null,
                                 message = CommonFunctions.getError(exception)
                             )*/
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage =
                                    jsonError.optString("messages", "Unknown HTTP error")
                                resultProductCategories.value =
                                    Resource.error(data = null, message = errorMessage)
                            } else {
                                resultProductCategories.value =
                                    Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultProductCategories.value =
                                Resource.error(data = null, message = e.message)
                        }
                    } else {
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

    fun addProduct() {
        resultAddProduct.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.addProduct(
                AddProductRequest(
                    listingPrice.get(),
                    createdBy.get(),
                    name.get(),
                    description.get(),
                    subCategoryId.get(),
                    spaDetailId.get(),
                    mainCategoryId.get(),
                    basePrice.get(),
                    stock.get(),
                    quantity.get()
                )
            )
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    /*   if (!CommonFunctions.getError(exception)!!.contains("401"))
                           resultAddProduct.value =
                               Resource.error(
                                   data = null,
                                   message = CommonFunctions.getError(exception)
                               )*/
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage =
                                    jsonError.optString("messages", "Unknown HTTP error")
                                resultAddProduct.value =
                                    Resource.error(data = null, message = errorMessage)
                            } else {
                                resultAddProduct.value =
                                    Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultAddProduct.value =
                                Resource.error(data = null, message = e.message)
                        }
                    } else {
                        resultAddProduct.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultAddProduct.value =
                            Resource.success(message = it.messages, data = it)
                    } else if (it?.statusCode == 201) {
                        resultAddProduct
                            .value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultAddProduct.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun updateProduct() {
        resultUpdateProduct.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.updateProduct(
                UpdateProductRequest(
                    listingPrice.get(),
                    productUpdateId.get(),
                    name.get(),
                    description.get(),
                    subCategoryId.get(),
                    spaDetailId.get(),
                    mainCategoryId.get(),
                    basePrice.get(),
                    stock.get(),
                    quantity.get()
                )
            )
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    /*    if (!CommonFunctions.getError(exception)!!.contains("401"))
                            resultUpdateProduct.value =
                                Resource.error(
                                    data = null,
                                    message = CommonFunctions.getError(exception)
                                )*/
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage =
                                    jsonError.optString("messages", "Unknown HTTP error")
                                resultUpdateProduct.value =
                                    Resource.error(data = null, message = errorMessage)
                            } else {
                                resultUpdateProduct.value =
                                    Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultUpdateProduct.value =
                                Resource.error(data = null, message = e.message)
                        }
                    } else {
                        resultUpdateProduct.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }

                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultUpdateProduct.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultUpdateProduct.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun uploadProductImage() {
        resultUploadProductImage.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.uploadProductImage(productId.get(), productImages.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    /*  if (!CommonFunctions.getError(exception)!!.contains("401"))
                          resultUploadProductImage.value =
                              Resource.error(
                                  data = null,
                                  message = CommonFunctions.getError(exception)
                              )*/
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage =
                                    jsonError.optString("messages", "Unknown HTTP error")
                                resultUploadProductImage.value =
                                    Resource.error(data = null, message = errorMessage)
                            } else {
                                resultUploadProductImage.value =
                                    Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultUploadProductImage.value =
                                Resource.error(data = null, message = e.message)
                        }
                    } else {
                        resultUploadProductImage.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultUploadProductImage.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultUploadProductImage.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

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


}