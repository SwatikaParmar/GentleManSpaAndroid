package com.app.gentlemanspa.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.gentlemanspa.network.InitialRepository
import com.app.gentlemanspa.ui.auth.fragment.forget.viewModel.ForgetPasswordViewModel
import com.app.gentlemanspa.ui.auth.fragment.login.viewModel.LoginViewModel
import com.app.gentlemanspa.ui.auth.fragment.otp.viewModel.OtpViewModel
import com.app.gentlemanspa.ui.auth.fragment.register.viewModel.RegisterViewModel
import com.app.gentlemanspa.ui.auth.fragment.setPassword.viewModel.SetPasswordViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.viewModel.AddressViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.anyProfessional.viewModel.AnyProfessionalViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.viewModel.CartViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.chat.viewModel.CustomerChatViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.chat.messages.viewModel.CustomerMessagesViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.messages.viewModel.ProfessionalMessagesViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.editAddress.viewModel.EditAddressViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.editProfile.viewModel.UpdateCustomerViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.event.viewModel.EventViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.viewModel.HistoryViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.viewModel.HomeCustomerViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.viewModel.MakeAppointmentViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.viewModel.MyOrdersViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.orderDetail.viewModel.OrderDetailsViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.payment.viewModel.PaymentViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.viewModel.ProductViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.productDetail.viewModel.ProductDetailViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.professionalTeamServices.viewModel.ProfessionalServicesViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.profile.viewModel.ProfileCustomerViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.viewModel.ProfessionalViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessionalService.viewModel.SelectProfessionalServiceViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.viewModel.ServiceViewModel
import com.app.gentlemanspa.ui.customerDashboard.fragment.serviceDetail.viewModel.ServiceDetailViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.viewModel.AddProductViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.chat.chat.viewModel.ProfessionalChatViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule.viewModel.CreateScheduleViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.viewModel.UpdateProfessionalViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.home.viewModel.HomeProfessionalViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.myService.viewModel.MyServiceViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.product.viewModel.ProductProfessionalViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.productDetail.viewModel.ProductDetailProfessionalViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.viewModel.ProfileProfessionalDetailViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.viewModel.ScheduleViewModel
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.viewModel.SelectCountryViewModel
import com.app.gentlemanspa.utils.updateStatus.UpdateStatusViewModel


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

            modelClass.isAssignableFrom(OtpViewModel::class.java) -> {
                return OtpViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(AddressViewModel::class.java) -> {
                return AddressViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java) -> {
                return ForgetPasswordViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SetPasswordViewModel::class.java) -> {
                return SetPasswordViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HomeProfessionalViewModel::class.java) -> {
                return HomeProfessionalViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfessionalServicesViewModel::class.java) -> {
                return ProfessionalServicesViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfileProfessionalDetailViewModel::class.java) -> {
                return ProfileProfessionalDetailViewModel(repository) as T
            }

            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                return CartViewModel(repository) as T
            }

            modelClass.isAssignableFrom(EditAddressViewModel::class.java) -> {
                return EditAddressViewModel(repository) as T
            }

            modelClass.isAssignableFrom(UpdateProfessionalViewModel::class.java) -> {
                return UpdateProfessionalViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SelectCountryViewModel::class.java) -> {
                return SelectCountryViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MakeAppointmentViewModel::class.java) -> {
                return MakeAppointmentViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HomeCustomerViewModel::class.java) -> {
                return HomeCustomerViewModel(repository) as T
            }

            modelClass.isAssignableFrom(UpdateCustomerViewModel::class.java) -> {
                return UpdateCustomerViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ServiceViewModel::class.java) -> {
                return ServiceViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SelectProfessionalServiceViewModel::class.java) -> {
                return SelectProfessionalServiceViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ServiceDetailViewModel::class.java) -> {
                return ServiceDetailViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfessionalViewModel::class.java) -> {
                return ProfessionalViewModel(repository) as T
            }

            modelClass.isAssignableFrom(AnyProfessionalViewModel::class.java) -> {
                return AnyProfessionalViewModel(repository) as T
            }


            modelClass.isAssignableFrom(ScheduleViewModel::class.java) -> {
                return ScheduleViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProductDetailViewModel::class.java) -> {
                return ProductDetailViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProductViewModel::class.java) -> {
                return ProductViewModel(repository) as T
            }

            modelClass.isAssignableFrom(AddProductViewModel::class.java) -> {
                return AddProductViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProductProfessionalViewModel::class.java) -> {
                return ProductProfessionalViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProductDetailProfessionalViewModel::class.java) -> {
                return ProductDetailProfessionalViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                return HistoryViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MyOrdersViewModel::class.java) -> {
                return MyOrdersViewModel(repository) as T
            }

            modelClass.isAssignableFrom(CreateScheduleViewModel::class.java) -> {
                return CreateScheduleViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MyServiceViewModel::class.java) -> {
                return MyServiceViewModel(repository) as T
            }


            modelClass.isAssignableFrom(ProfileCustomerViewModel::class.java) -> {
                return ProfileCustomerViewModel(repository) as T
            }

            modelClass.isAssignableFrom(OrderDetailsViewModel::class.java) -> {
                return OrderDetailsViewModel(repository) as T
            }

            modelClass.isAssignableFrom(EventViewModel::class.java) -> {
                return EventViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CustomerMessagesViewModel::class.java) -> {
                return CustomerMessagesViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CustomerChatViewModel::class.java) -> {
                return CustomerChatViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfessionalMessagesViewModel::class.java) -> {
                return ProfessionalMessagesViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfessionalChatViewModel::class.java) -> {
                return ProfessionalChatViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UpdateStatusViewModel::class.java) -> {
                return UpdateStatusViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PaymentViewModel::class.java) -> {
                return PaymentViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Class not found")
        }
    }

}