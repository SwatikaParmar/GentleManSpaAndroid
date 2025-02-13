package com.app.gentlemanspa.ui.customerDashboard.fragment.home.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.BannerResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.NotificationCountResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductCategoriesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.SpaCategoriesResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.model.GetProfessionalDetailResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class HomeCustomerViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val search = ObservableField<String>()
    val spaDetailId = ObservableField<Int>()
    val userId = ObservableField<String>()
    val resultBanner = MutableLiveData<Resource<BannerResponse>>()
    val resultCategories = MutableLiveData<Resource<SpaCategoriesResponse>>()
    val resultProductCategories = MutableLiveData<Resource<ProductCategoriesResponse>>()
    val resultProfileCustomerDetail = MutableLiveData<Resource<GetProfessionalDetailResponse>>()
    val resultProfessionalTeam= MutableLiveData<Resource<ProfessionalResponse>>()
    val resultUpcomingAppointmentList = MutableLiveData<Resource<UpcomingServiceAppointmentResponse>>()
    val resultNotificationCount = MutableLiveData<Resource<NotificationCountResponse>>()


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
            initialRepository.getSpaCategories(spaDetailId.get()!!)
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
    fun getProductCategories() {
        resultProductCategories.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProductCategories()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultProductCategories.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
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
    fun getCustomerDetail() {
        resultProfileCustomerDetail.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getCustomerDetail(userId.get()!!)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultProfileCustomerDetail.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultProfileCustomerDetail.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultProfileCustomerDetail.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
    fun getProfessionalTeamList() {
        Log.d("professionalTeam","inside getProfessionalTeamList()")

        resultProfessionalTeam.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProfessionalTeamList()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultProfessionalTeam.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultProfessionalTeam.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultProfileCustomerDetail.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
    fun getUpcomingAppointments() {
        resultUpcomingAppointmentList.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getServiceAppointments(userId.get()!!,"Upcoming",
                1000,
                1,
            )
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultUpcomingAppointmentList.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        Log.d("dataList","inside viewModel dataList size ${it.data.dataList.size} ")

                        resultUpcomingAppointmentList.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultUpcomingAppointmentList.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
    fun getNotificationCount() {
        resultNotificationCount.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getNotificationCount()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultNotificationCount.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultNotificationCount.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultNotificationCount.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultNotificationCount.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultNotificationCount.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultNotificationCount.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}