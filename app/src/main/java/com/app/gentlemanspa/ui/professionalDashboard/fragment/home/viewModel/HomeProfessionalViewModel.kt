package com.app.gentlemanspa.ui.professionalDashboard.fragment.home.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.AddUserToChatRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.AddUserToChatResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.model.GetProfessionalDetailResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class HomeProfessionalViewModel(private var initialRepository: InitialRepository): AndroidViewModel(Application()) {

    val type = ObservableField<String>()
    val professionalDetailId = ObservableField<Int>()
    val resultProfileProfessionalDetailAccount = MutableLiveData<Resource<GetProfessionalDetailResponse>>()
    val resultAppointmentList = MutableLiveData<Resource<UpcomingServiceAppointmentResponse>>()
    val resultAddUserToChat= MutableLiveData<Resource<AddUserToChatResponse>>()


    fun getProfessionalDetail() {
        resultProfileProfessionalDetailAccount.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProfessionalDetail()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultProfileProfessionalDetailAccount.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultProfileProfessionalDetailAccount.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultProfileProfessionalDetailAccount.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultProfileProfessionalDetailAccount.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultProfileProfessionalDetailAccount.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultProfileProfessionalDetailAccount.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun getAppointmentListApi() {
        resultAppointmentList.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getAppointmentsList(type.get()!!,
                1000,
                1,
                professionalDetailId.get()!!
            )
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultAppointmentList.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultAppointmentList.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultAppointmentList.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultAppointmentList.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        Log.d("dataList","inside viewModel dataList size ${it.data.dataList.size} ")

                        resultAppointmentList.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultAppointmentList.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }



    }
    fun addUserToChatApi(addUserToChatRequest: AddUserToChatRequest) {
        resultAddUserToChat.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.addUserToChat(addUserToChatRequest)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultAddUserToChat.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        //  Log.d("dataList","inside viewModel dataList size ${it.data.dataList.size} ")

                        resultAddUserToChat.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultAddUserToChat.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}

