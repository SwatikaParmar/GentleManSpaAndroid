package com.app.gentlemanspa.ui.customerDashboard.fragment.anyProfessional.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalResponse
import com.app.gentlemanspa.utils.CommonFunctions
import com.app.gentlemanspa.utils.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AnyProfessionalViewModel  (private var initialRepository: InitialRepository) : AndroidViewModel(
    Application()
) {

    val spaServiceId = ObservableField<Int>()
    val spaDetailId = ObservableField<Int>()
    val searchQuery = ObservableField<String>()
    val resultProfessionalList= MutableLiveData<Resource<ProfessionalResponse>>()




    fun getProfessionalList() {
        resultProfessionalList.value = Resource.loading(null)
        viewModelScope.launch {
            initialRepository.getProfessionalList(spaServiceId.get(),spaDetailId.get(),searchQuery.get())
                .onStart { }
                .onCompletion { }
                .catch { exception ->
                    if (!CommonFunctions.getError(exception)!!.contains("401"))
                        resultProfessionalList.value =
                            Resource.error(
                                data = null,
                                message = CommonFunctions.getError(exception)
                            )
                }
                .collect {
                    if (it?.statusCode == 200) {
                        resultProfessionalList.value =
                            Resource.success(message = it.messages, data = it)
                    } else {
                        resultProfessionalList.value =
                            Resource.error(data = null, message = it?.messages)
                    }
                }
        }
    }

}