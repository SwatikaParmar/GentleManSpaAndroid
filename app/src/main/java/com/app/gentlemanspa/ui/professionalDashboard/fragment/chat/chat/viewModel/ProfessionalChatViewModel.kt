package com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.chat.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model.CustomerChatMessageHistoryResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model.CustomerSendMessageRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model.CustomerSendMessageResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.model.DeleteMessageResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class ProfessionalChatViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val senderId= ObservableField<String>()
    val receiverId= ObservableField<String>()
    val messageId= ObservableField<Int>()
    val resultCustomerChatHistoryList = MutableLiveData<Resource<CustomerChatMessageHistoryResponse>>()
    val resultCustomerSendMessage = MutableLiveData<Resource<CustomerSendMessageResponse>>()
    val resultDeleteMessage = MutableLiveData<Resource<DeleteMessageResponse>>()


    fun getCustomerChatHistoryApi() {
        resultCustomerChatHistoryList.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getCustomerChatHistory(senderId.get()!!,receiverId.get()!!,1,1000)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultCustomerChatHistoryList.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultCustomerChatHistoryList.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultCustomerChatHistoryList.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultCustomerChatHistoryList.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultCustomerChatHistoryList.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultCustomerChatHistoryList.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun customerSendMessageApi(customerSendMessageRequest: CustomerSendMessageRequest) {
        resultCustomerSendMessage.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.customerSendMessage(customerSendMessageRequest)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultCustomerSendMessage.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultCustomerSendMessage.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultCustomerSendMessage.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultCustomerSendMessage.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultCustomerSendMessage.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultCustomerSendMessage.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun deleteMessageApi() {
        resultDeleteMessage.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.deleteMessage(messageId.get()!!)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultDeleteMessage.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultDeleteMessage.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultDeleteMessage.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultDeleteMessage.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultDeleteMessage.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultDeleteMessage.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}