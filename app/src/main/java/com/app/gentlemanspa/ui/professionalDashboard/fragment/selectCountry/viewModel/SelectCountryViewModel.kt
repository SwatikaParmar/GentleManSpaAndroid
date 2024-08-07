package com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.model.country.CountryResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.model.states.StatesResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SelectCountryViewModel(var initialRepository: InitialRepository) :
    AndroidViewModel(Application()) {

    val search = ObservableField<String>()

    val countryId = ObservableField<Int>()

    val resultCountry = MutableLiveData<Resource<CountryResponse>>()
    val resultStates = MutableLiveData<Resource<StatesResponse>>()


    fun getCountry() {
        resultCountry.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getCountry(search.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultCountry.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultCountry.value = Resource.success(message = it?.messages, data = it)
                    } else {
                        resultCountry.value = Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

    fun getStates() {
        resultStates.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getStates(countryId.get(), search.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultStates.value = Resource.error(
                            data = null,
                            message = CommonFunctions.getError(exception)
                        )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultStates.value = Resource.success(message = it?.messages, data = it)
                    } else {
                        resultStates.value = Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }



}