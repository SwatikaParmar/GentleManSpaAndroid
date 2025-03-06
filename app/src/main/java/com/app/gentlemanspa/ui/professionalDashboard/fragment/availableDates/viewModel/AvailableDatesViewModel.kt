package com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.model.AvailableDatesResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.model.BlockDatesResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.model.SlotStatusRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.availableDates.model.SlotStatusResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException


class AvailableDatesViewModel  (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {
    val professionalId= ObservableField<Int>()
    val availabilityState= ObservableField<String>()
    val resultProfessionalBlockDates= MutableLiveData<Resource<BlockDatesResponse>>()
    val resultExpertAvailableDates= MutableLiveData<Resource<AvailableDatesResponse>>()
    val resultSlotStatus= MutableLiveData<Resource<SlotStatusResponse>>()

    fun getProfessionalBlockDates() {
        resultProfessionalBlockDates.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProfessionalBlockDates(professionalId.get()!!,availabilityState.get()!!)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage =
                                    jsonError.optString("messages", "Unknown HTTP error")
                                resultProfessionalBlockDates.value =
                                    Resource.error(data = null, message = errorMessage)
                            } else {
                                resultProfessionalBlockDates.value =
                                    Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultProfessionalBlockDates.value =
                                Resource.error(data = null, message = e.message)
                        }
                    } else {
                        resultProfessionalBlockDates.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultProfessionalBlockDates.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultProfessionalBlockDates.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
    fun getProfessionalAvailableDates() {
        resultExpertAvailableDates.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProfessionalAvailableDates(professionalId.get()!!)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage =
                                    jsonError.optString("messages", "Unknown HTTP error")
                                resultExpertAvailableDates.value =
                                    Resource.error(data = null, message = errorMessage)
                            } else {
                                resultExpertAvailableDates.value =
                                    Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultExpertAvailableDates.value =
                                Resource.error(data = null, message = e.message)
                        }
                    } else {
                        resultExpertAvailableDates.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultExpertAvailableDates.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultExpertAvailableDates.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
    fun slotStatusApi(request: SlotStatusRequest) {
        resultSlotStatus.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.slotStatus(request)
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (exception is HttpException) {
                        try {
                            val errorBody = exception.response()?.errorBody()?.string()
                            if (!errorBody.isNullOrEmpty()) {
                                val jsonError = JSONObject(errorBody)
                                val errorMessage =
                                    jsonError.optString("messages", "Unknown HTTP error")
                                resultSlotStatus.value =
                                    Resource.error(data = null, message = errorMessage)
                            } else {
                                resultSlotStatus.value =
                                    Resource.error(data = null, message = "Unknown HTTP error")
                            }
                        } catch (e: Exception) {
                            resultSlotStatus.value =
                                Resource.error(data = null, message = e.message)
                        }
                    } else {
                        resultSlotStatus.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                    }
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultSlotStatus.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultSlotStatus.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }
}