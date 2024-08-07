package com.app.gentlemanspa.ui.customerDashboard.fragment.home.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.BannerResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.CategoriesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.LocationResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeCustomerViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val search = ObservableField<String>()
    val resultLocationAddress = MutableLiveData<Resource<LocationResponse>>()
    val resultSearchLocationAddress = MutableLiveData<Resource<LocationResponse>>()
    val resultBanner = MutableLiveData<Resource<BannerResponse>>()
    val resultCategories = MutableLiveData<Resource<CategoriesResponse>>()


    fun getLocationAddress() {
        resultLocationAddress.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getLocationAddress()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultLocationAddress.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultLocationAddress.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultLocationAddress.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }


    fun getSearchLocationAddress() {
        resultLocationAddress.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getSearchLocationAddress(search.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultLocationAddress.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultLocationAddress.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultLocationAddress.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun getBanner() {
        resultBanner.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getBanner()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultBanner.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultBanner.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultBanner.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

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



}