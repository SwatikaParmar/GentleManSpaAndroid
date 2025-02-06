package com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.messages.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.model.CustomerMessagesResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class ProfessionalMessagesViewModel(private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val userId=ObservableField<String>()
    val resultCustomerMessagesList = MutableLiveData<Resource<CustomerMessagesResponse>>()


    fun getCustomerMessagesListApi() {
        resultCustomerMessagesList.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getCustomerMessagesList(userId.get()!!)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultCustomerMessagesList.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultCustomerMessagesList.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultCustomerMessagesList.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultCustomerMessagesList.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultCustomerMessagesList.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultCustomerMessagesList.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}