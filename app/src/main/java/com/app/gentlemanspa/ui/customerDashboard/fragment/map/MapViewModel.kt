package com.app.gentlemanspa.ui.customerDashboard.fragment.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.google.gson.JsonObject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MapViewModel(application: Application) : AndroidViewModel(application) {
/*
    val webService : WebService
    val initialRepository : InitialRepository
    val responseSetCustomerAddressStatus : MutableLiveData<WebResponse<ResponseSetCustomerAddressStatus>> = MutableLiveData()
    var headers :HashMap<String,String> = HashMap()

    init {
        webService =(application as MyApplication).getWebServiceInstance()
        initialRepository = InitialRepository(webService)
    }


    fun setCustomerAddressStatus(
        customerAddressId :Int,
        status :Boolean
    ){
        viewModelScope.launch {
            val jsonObject = JsonObject()
            jsonObject.addProperty("customerAddressId",customerAddressId)
            jsonObject.addProperty("status",status)
            initialRepository.setCustomerAddressStatus(headers,jsonObject)
                .onStart {  }
                .onCompletion {  }
                .catch { exception ->
                    responseSetCustomerAddressStatus.value =WebResponse(Status.AUTHORISATION,null,
                        getError(exception),false)
                }
                .collect{
                    responseSetCustomerAddressStatus.value =WebResponse(it.statusCode,it,it.messages,it.isSuccess)
                }
        }
    }
*/

}