package com.app.gentlemanspa.ui.customerDashboard.fragment.editAddress.viewModel

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.CustomerAddressResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.editAddress.model.AddCustomerAddressRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.editAddress.model.AddCustomerAddressResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


class EditAddressViewModel(private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()){

    val resultCustomerAddress = MutableLiveData<Resource<CustomerAddressResponse>>()
    val resultAddCustomerAddress = MutableLiveData<Resource<AddCustomerAddressResponse>>()
    val resultUpdateCustomerAddress = MutableLiveData<Resource<AddCustomerAddressResponse>>()

    fun addCustomerAddress(customerAddressId:Int,receiverName:String,receiverPhone:String,pinCode:String,adminArea:String
    ,city:String,state:String,street:String,nearbyLandMark:String,
                           addressType:String,latitude:String,longitude:String,alternativePhoneNo:String) {
        resultAddCustomerAddress.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.geCustomerAddressList(AddCustomerAddressRequest(customerAddressId,receiverName,receiverPhone,
                    adminArea,street,nearbyLandMark,pinCode,alternativePhoneNo,addressType,city,
                state,latitude,longitude))
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultAddCustomerAddress.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultAddCustomerAddress.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultAddCustomerAddress.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }


    fun updateCustomerAddress(customerAddressId:Int,receiverName:String,receiverPhone:String,pinCode:String,adminArea:String
                           ,city:String,state:String,street:String,nearbyLandMark:String,
                           addressType:String,latitude:String,longitude:String,alternativePhoneNo:String) {
        resultUpdateCustomerAddress.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.updateCustomerAddress(AddCustomerAddressRequest(customerAddressId,receiverName,receiverPhone,
                adminArea,street,nearbyLandMark,pinCode,alternativePhoneNo,addressType,city,
                state,latitude,longitude))
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultUpdateCustomerAddress.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultUpdateCustomerAddress.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultUpdateCustomerAddress.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }



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



 /*   fun UpdateCustomerAddress(
        customerAddressId: Int,
        fullName: String,
        phoneNumber: String,
        alternatePhoneNumber: String,
        pincode: String,
        state: String,
        city: String,
        houseNoOrBuildingName: String,
        streetAddresss: String,
        nearbyLandMark: String,
        addressType: String?,
        addressLatitude: String?,
        addressLongotude: String?

    ){
        viewModelScope.launch {
            Log.d(TAG, "addCustomerAddressViewModelLatitude: ${addressLatitude}")
            Log.d(TAG, "addCustomerAddressViewModelLongitude: ${addressLongotude}")
            val jsonObject =JsonObject()
            jsonObject.addProperty( "customerAddressId",customerAddressId)
            jsonObject.addProperty( "fullName",fullName)
            jsonObject.addProperty( "phoneNumber",phoneNumber)
            jsonObject.addProperty( "alternatePhoneNumber",alternatePhoneNumber)
            jsonObject.addProperty( "pincode",pincode)
            jsonObject.addProperty( "state",state)
            jsonObject.addProperty( "city",city)
            jsonObject.addProperty( "houseNoOrBuildingName",houseNoOrBuildingName)
            jsonObject.addProperty("streetAddresss",streetAddresss)
            jsonObject.addProperty("nearbyLandMark",nearbyLandMark)
            jsonObject.addProperty("addressType",addressType)
            jsonObject.addProperty("addressLatitude",addressLatitude)
            jsonObject.addProperty("addressLongitude",addressLongotude)

            Log.d(TAG, "addCustomerAddressViewModel: ${jsonObject}")
            initialRepository.updateCustomerAddress(headers,jsonObject)
                .onStart {  }
                .onCompletion {  }
                .catch { exception ->
                    responseUpdateCustomerAddress.value =WebResponse(Status.AUTHORISATION,null,getError(exception),false)
                }
                .collect{
                    responseUpdateCustomerAddress.value =WebResponse(it.statusCode,it,it.messages,it.isSuccess)
                }
        }
    }


    fun getCustomerAddressList(){
        viewModelScope.launch {
            initialRepository.getAddressList(headers)
                .onStart {  }
                .onCompletion {  }
                .catch { exception ->
                    responseCustomerAddressList.value =
                        WebResponse(Status.AUTHORISATION,null,getError(exception),false)
                }
                .collect{
                    responseCustomerAddressList.value =
                        WebResponse(it.statusCode,it,it.messages,it.isSuccess)
                }
        }
    }


    fun getCustomerAddressDetail(addressId :Int){
        viewModelScope.launch {
            initialRepository.getAddressDetail(headers,addressId)
                .onStart {
                    responseCustomerAddressLoading.value =true
                }
                .onCompletion {
                    responseCustomerAddressLoading.value =false
                }
                .catch { exception ->
                    responseCustomerAddressDetail.value =WebResponse(Status.AUTHORISATION,null,getError(exception),false)
                }
                .collect{
                    responseCustomerAddressDetail.value =WebResponse(it.statusCode,it,it.messages,it.isSuccess)
                }
        }
    }*/
}