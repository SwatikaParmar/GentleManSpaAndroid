package com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule.model.AddUpdateProfessionalScheduleRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule.model.AddUpdateProfessionalScheduleResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.SchedulesByProfessionalDetailIdResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.WeekDaysResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class CreateScheduleViewModel(private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val professionalDetailId= ObservableField<Int>()
    val professionalScheduleId= ObservableField<Int>()
    val weekDaysId= ObservableField<Int>()
    val fromTime= ObservableField<String>()
    val toTime= ObservableField<String>()
    val resultAddUpdateProfessionalSchedule= MutableLiveData<Resource<AddUpdateProfessionalScheduleResponse>>()

    fun addUpdateProfessionalSchedule(request:AddUpdateProfessionalScheduleRequest) {
        resultAddUpdateProfessionalSchedule.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.addUpdateProfessionalSchedule(request)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage = jsonError.optString("messages", "Unknown HTTP error")
                                resultAddUpdateProfessionalSchedule.value = Resource.error(data = null, message = errorMessage)
                            } else {
                                resultAddUpdateProfessionalSchedule.value = Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultAddUpdateProfessionalSchedule.value = Resource.error(data = null, message = e.message)
                        }
                    }else{
                        resultAddUpdateProfessionalSchedule.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultAddUpdateProfessionalSchedule.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultAddUpdateProfessionalSchedule.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}