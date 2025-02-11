package com.app.gentlemanspa.ui.customerDashboard.fragment.notification.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.notification.model.NotificationListResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CustomerNotificationViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val searchQuery = ObservableField<String>()
    val resultCustomerNotification = MutableLiveData<Resource<NotificationListResponse>>()



    fun getNotificationListApi() {
        resultCustomerNotification.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getNotificationList(
                1,
                1000
            )
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultCustomerNotification.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        Log.d("dataList","inside viewModel dataList size ${it.data.dataList.size} ")

                        resultCustomerNotification.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultCustomerNotification.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }



}