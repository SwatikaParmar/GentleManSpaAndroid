package com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.SchedulesByProfessionalDetailIdResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.WeekDaysResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ScheduleViewModel (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val professionalDetailId=ObservableField<Int>()
    val resultWeekDays = MutableLiveData<Resource<WeekDaysResponse>>()
    val resultSchedulesByProfessionalDetailId= MutableLiveData<Resource<SchedulesByProfessionalDetailIdResponse>>()
    fun getWeekDays() {
        resultWeekDays.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getWeekDays()
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultWeekDays.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultWeekDays.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultWeekDays.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun getSchedulesByProfessionalDetailId() {
        resultSchedulesByProfessionalDetailId.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getSchedulesByProfessionalDetailId(professionalDetailId.get()!!)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultSchedulesByProfessionalDetailId.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultSchedulesByProfessionalDetailId.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultSchedulesByProfessionalDetailId.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}