package com.app.gentlemanspa.ui.customerDashboard.fragment.notification.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.notification.model.NotificationListResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.notification.model.ReadNotificationResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class CustomerNotificationViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val searchQuery = ObservableField<String>()
    val notificationSentId = ObservableField<Int>()
    val resultNotificationList = MutableLiveData<Resource<NotificationListResponse>>()
    val resultReadNotification = MutableLiveData<Resource<ReadNotificationResponse>>()



    fun getNotificationListApi() {
        resultNotificationList.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getNotificationList(
                1,
                1000
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
                                resultNotificationList.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultNotificationList.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultNotificationList.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultNotificationList.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        Log.d("dataList","inside viewModel dataList size ${it.data.dataList.size} ")

                        resultNotificationList.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultNotificationList.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun readNotificationApi() {
        resultReadNotification.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.readNotification(notificationSentId.get()!!)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                        if (exception is HttpException) {
                            try {
                                val errorBody = exception.response()?.errorBody()?.string()
                                if (!errorBody.isNullOrEmpty()) {
                                    val jsonError = JSONObject(errorBody)
                                    val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                    resultReadNotification.value = Resource.error(data = null, message = errorMessage)
                                } else {
                                    resultReadNotification.value = Resource.error(data = null, message = "Unknown HTTP error")
                                }
                            } catch (e: Exception) {
                                resultReadNotification.value = Resource.error(data = null, message = e.message)
                            }
                        }else{
                            resultReadNotification.value =
                                Resource.error(
                                    data = null,
                                    message = CommonFunctions.getError(exception)
                                )
                        }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultReadNotification.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultReadNotification.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }


}