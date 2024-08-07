package com.app.gentlemanspa.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.auth.fragment.login.viewModel.LoginViewModel
import com.app.gentlemanspa.ui.auth.fragment.register.viewModel.RegisterViewModel
import com.app.gentlemanspa.ui.auth.fragment.setPassword.viewModel.SetPasswordViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.viewModel.HomeCustomerViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.viewModel.ProfessionalViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.viewModel.ServiceViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.serviceDetail.viewModel.ServiceDetailViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.viewModel.UpdateProfessionalViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.viewModel.ProfileProfessionalDetailViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.viewModel.SelectCountryViewModel


@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: InitialRepository
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                return RegisterViewModel(repository) as T
            }

             modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                 return LoginViewModel(repository) as T
             }

             modelClass.isAssignableFrom(SetPasswordViewModel::class.java) -> {
                 return SetPasswordViewModel(repository) as T
             }

             modelClass.isAssignableFrom(ProfileProfessionalDetailViewModel::class.java) -> {
                 return ProfileProfessionalDetailViewModel(repository) as T
             }

             modelClass.isAssignableFrom(UpdateProfessionalViewModel::class.java) -> {
                 return UpdateProfessionalViewModel(repository) as T
             }




            modelClass.isAssignableFrom(SelectCountryViewModel::class.java) -> {
                return SelectCountryViewModel(repository) as T
            }


            modelClass.isAssignableFrom(HomeCustomerViewModel::class.java) -> {
                return HomeCustomerViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ServiceViewModel::class.java) -> {
                return ServiceViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ServiceDetailViewModel::class.java) -> {
                return ServiceDetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfessionalViewModel::class.java) -> {
                return ProfessionalViewModel(repository) as T
            }

           /*
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ServiceDetailViewModel::class.java) -> {
                return ServiceDetailViewModel(repository) as T
            }

            modelClass.isAssignableFrom(FindDoctorViewModel::class.java) -> {
                return FindDoctorViewModel(repository) as T
            }*/





            else -> throw IllegalArgumentException("Class not found")
        }
    }

}