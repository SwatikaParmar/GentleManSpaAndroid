package com.app.gentlemanspa.network



import android.util.Log
import com.app.gentlemanspa.ui.auth.fragment.forget.model.ForgetPasswordRequest
import com.app.gentlemanspa.ui.auth.fragment.forget.model.ForgetPasswordResponse
import com.app.gentlemanspa.ui.auth.fragment.login.model.LoginRequest
import com.app.gentlemanspa.ui.auth.fragment.login.model.LoginResponse
import com.app.gentlemanspa.ui.auth.fragment.register.model.EmailOtpRequest
import com.app.gentlemanspa.ui.auth.fragment.register.model.EmailOtpResponse
import com.app.gentlemanspa.ui.auth.fragment.register.model.PhoneUniqueRequest
import com.app.gentlemanspa.ui.auth.fragment.register.model.ProfileRegisterResponse
import com.app.gentlemanspa.ui.auth.fragment.otp.model.SignUpRequest
import com.app.gentlemanspa.ui.auth.fragment.otp.model.SignUpResponse
import com.app.gentlemanspa.ui.auth.fragment.setPassword.model.ChangePasswordRequest
import com.app.gentlemanspa.ui.auth.fragment.setPassword.model.ChangePasswordResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.CustomerAddressResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.CustomerAddressStatusRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.CustomerAddressStatusResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.address.model.DeleteAddressResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.model.CustomerPlaceOrderRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.cart.model.CustomerPlaceOrderResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.UpcomingServiceAppointmentResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.editAddress.model.AddCustomerAddressRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.editAddress.model.AddCustomerAddressResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.editProfile.model.UpdateProfileCustomerRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.editProfile.model.UpdateProfileCustomerResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.CancelUpcomingAppointmentRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.history.model.CancelUpcomingAppointmentResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.BannerResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.LocationResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model.SpecialityResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.model.GetProfessionalDetailResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model.UpdateProfileProfessionalRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model.UpdateProfileProfessionalResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.CategoriesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductCategoriesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.model.ServiceGetAvailableDatesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.model.ServiceGetAvailableTimeSlotsResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.model.ServiceRescheduleRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.makeAppointment.model.ServiceRescheduleResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.myOrders.model.MyOrdersResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.model.AddProductInCartRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.product.model.AddProductInCartResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.productDetail.model.ProductDetailResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.professionalTeam.model.ProfessionalServicesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.AddServiceToCartRequest
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.AddServiceToCartResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.GetCartItemsResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.ServiceResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.serviceDetail.model.ServiceDetailResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.AddProductRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.AddProductResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.UpdateProductRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.UpdateProductResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.UploadProductImageResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule.model.AddUpdateProfessionalScheduleRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.createSchedule.model.AddUpdateProfessionalScheduleResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.product.model.DeleteProductResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.SchedulesByProfessionalDetailIdResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.WeekDaysResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.model.country.CountryResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.model.states.StatesResponse
import com.app.gentlemanspa.utils.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody


class InitialRepository {
    suspend fun registerAccount(body: SignUpRequest?): Flow<SignUpResponse?> {
        return flow {
            val result = Api.apiInterface?.registerAccount(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun profilePicRegister(
        profilePic: MultipartBody.Part?,
        id: RequestBody?
    ): Flow<ProfileRegisterResponse?> {
        return flow {
            val result = Api.apiInterface?.profilePicRegister(profilePic, id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun loginAccount(body: LoginRequest?): Flow<LoginResponse?> {
        return flow {
            val result = Api.apiInterface?.loginAccount(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setChangePassword(body: ChangePasswordRequest?): Flow<ChangePasswordResponse?> {
        return flow {
            val result = Api.apiInterface?.setChangePassword(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun resetPassword(body: ForgetPasswordRequest?): Flow<ForgetPasswordResponse?> {
        return flow {
            val result = Api.apiInterface?.resetPassword(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getProfessionalDetail(): Flow<GetProfessionalDetailResponse?> {
        return flow {
            val result = Api.apiInterface?.getProfessionalDetail()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun updateProfessional(body: UpdateProfileProfessionalRequest?): Flow<UpdateProfileProfessionalResponse?> {
        return flow {
            val result = Api.apiInterface?.updateProfessional(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCustomerDetail(): Flow<GetProfessionalDetailResponse?> {
        return flow {
            val result = Api.apiInterface?.getCustomerDetail()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateCustomerDetail(body: UpdateProfileCustomerRequest?): Flow<UpdateProfileCustomerResponse?> {
        return flow {
            Log.d("UpdateProfileBody","Body is->${body}")
            val result = Api.apiInterface?.updateCustomerProfile(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCountry(search: String?): Flow<CountryResponse?> {
        return flow {
            val result =Api.apiInterface?.getCountry(search)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getStates(countryId: Int?,search: String?): Flow<StatesResponse?> {
        return flow {
            val result =Api.apiInterface?.getStates(countryId,search)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getSpeciality(): Flow<SpecialityResponse?> {
        return flow {
            val result =Api.apiInterface?.getSpeciality()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCategories(): Flow<CategoriesResponse?> {
        return flow {
            val result =Api.apiInterface?.getCategories()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProductCategories(): Flow<ProductCategoriesResponse?> {
        return flow {
            val result =Api.apiInterface?.getProductCategories()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getBanner(): Flow<BannerResponse?> {
        return flow {
            val result =Api.apiInterface?.getBanner()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLocationAddress(): Flow<LocationResponse?> {
        return flow {
            val result =Api.apiInterface?.getLocationAddress()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSearchLocationAddress(search: String?): Flow<LocationResponse?> {
        return flow {
            val result =Api.apiInterface?.getSearchLocationAddress(search)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getServiceList(pageNumber: Int?, pageSize: Int?,categoryId: Int?,searchQuery:String, spaDetailId:Int?): Flow<ServiceResponse?> {
        return flow {
            val result =Api.apiInterface?.getServiceList(pageNumber,pageSize,categoryId,searchQuery,spaDetailId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProfessionalServiceList(professionalDetailId: Int?): Flow<ProfessionalServicesResponse?> {
        return flow {
            val result =Api.apiInterface?.getProfessionalServiceList(professionalDetailId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getServiceDetail(serviceId: Int?,spaDetailId: Int?): Flow<ServiceDetailResponse?> {
        return flow {
            val result =Api.apiInterface?.getServiceDetail(serviceId,spaDetailId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun getServiceAvailableDates(spaServiceIds: Int?,professionalId: String?): Flow<ServiceGetAvailableDatesResponse?> {
        Log.d("AvailableDatesResponse","inside InitialRepository spaServiceIds->${spaServiceIds} professionalId->$professionalId")

        return flow {
            val result =Api.apiInterface?.getServiceAvailableDates(spaServiceIds,professionalId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun getServiceAvailableTimeSlots(spaServiceIds: Int?,date:String,professionalId: String?): Flow<ServiceGetAvailableTimeSlotsResponse?> {
        Log.d("AvailableTimeResponse","inside InitialRepository spaServiceIds->${spaServiceIds}date->${date} professionalId->$professionalId")

        return flow {
            val result =Api.apiInterface?.getServiceAvailableTimeSlots(spaServiceIds,date,professionalId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun getProfessionalList(spaServiceId: Int?,spaDetailId: Int?): Flow<ProfessionalResponse?> {
        Log.d("professionalList","inside InitialRepository spaServiceId->${spaServiceId} spaDetailId->$spaDetailId")

        return flow {
            val result =Api.apiInterface?.getProfessionalList(spaServiceId,spaDetailId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProfessionalTeamList(): Flow<ProfessionalResponse?> {

        return flow {
            val result =Api.apiInterface?.getProfessionalTeamList()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getWeekDays(): Flow<WeekDaysResponse?> {
        return flow {
            val result =Api.apiInterface?.getWeekDays()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getProductsList(pageNumber: Int?,pageSize: Int?,mainCategoryId: Int?,searchQuery:String,spaDetailId:Int): Flow<ProductsResponse?> {
        return flow {
            val result =Api.apiInterface?.getProductsList(pageNumber,pageSize,mainCategoryId,searchQuery,spaDetailId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getServiceCartItems(): Flow<GetCartItemsResponse?> {
        return flow {
            val result =Api.apiInterface?.getServiceCartItems()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun geCustomerAddressList(): Flow<CustomerAddressResponse?> {
        return flow {
            val result =Api.apiInterface?.geCustomerAddressList()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun geCustomerAddressList(body:AddCustomerAddressRequest): Flow<AddCustomerAddressResponse?> {
        return flow {
            val result =Api.apiInterface?.addCustomerAddress(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun updateCustomerAddress(body:AddCustomerAddressRequest): Flow<AddCustomerAddressResponse?> {
        return flow {
            val result =Api.apiInterface?.updateCustomerAddress(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun setCustomerAddressStatus(body:CustomerAddressStatusRequest): Flow<CustomerAddressStatusResponse?> {
        return flow {
            val result =Api.apiInterface?.setCustomerAddressStatus(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun deleteCustomerAddress(customerAddressId:Int): Flow<DeleteAddressResponse?> {
        return flow {
            val result =Api.apiInterface?.deleteCustomerAddress(customerAddressId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun addServiceToCart(body: AddServiceToCartRequest?): Flow<AddServiceToCartResponse?> {
        return flow {
            val result = Api.apiInterface?.addServiceToCart(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun addProductInCart(body: AddProductInCartRequest?): Flow<AddProductInCartResponse?> {
        return flow {
            val result = Api.apiInterface?.addProductInCart(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun getProductDetails(id: Int?): Flow<ProductDetailResponse?> {
        return flow {
            val result =Api.apiInterface?.getProductDetails(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun addProduct(body: AddProductRequest): Flow<AddProductResponse?> {
        return flow {
            val result =Api.apiInterface?.addProduct(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun updateProduct(body: UpdateProductRequest): Flow<UpdateProductResponse?> {
        return flow {
            val result =Api.apiInterface?.updateProduct(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun uploadProductImage(projectId : RequestBody?, productImages: ArrayList<MultipartBody.Part>?):Flow<UploadProductImageResponse?>{
        return flow {
            val result =Api.apiInterface?.uploadProductImage(projectId,productImages)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDeleteProduct(id: Int?): Flow<DeleteProductResponse?> {
        return flow {
            val result =Api.apiInterface?.getDeleteProduct(id)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

     suspend fun emailOtp(body: EmailOtpRequest?): Flow<EmailOtpResponse?> {
         return flow {
             val result = Api.apiInterface?.emailOtp(body)
             emit(result)
         }.flowOn(Dispatchers.IO)
     }
    suspend fun phoneUnique(body: PhoneUniqueRequest?): Flow<EmailOtpResponse?> {
        return flow {
            val result = Api.apiInterface?.phoneUnique(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getServiceAppointments(type:String, pageSize: Int?,pageNumber: Int?): Flow<UpcomingServiceAppointmentResponse?> {
        return flow {
            val result =Api.apiInterface?.getServiceAppointments(type,pageSize,pageNumber)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun cancelUpcomingAppointment(body:CancelUpcomingAppointmentRequest): Flow<CancelUpcomingAppointmentResponse?> {
        return flow {
            val result =Api.apiInterface?.cancelUpcomingAppointment(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun customerPlaceOrder(body:CustomerPlaceOrderRequest): Flow<CustomerPlaceOrderResponse?> {
        return flow {
            val result =Api.apiInterface?.customerPlaceOrder(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    suspend fun getOrderedProducts(type:String, pageSize: Int?,pageNumber: Int?): Flow<MyOrdersResponse?> {
        return flow {
            val result =Api.apiInterface?.getOrderedProducts(type,pageSize,pageNumber)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun serviceReschedule(body:ServiceRescheduleRequest): Flow<ServiceRescheduleResponse?> {
        return flow {
            val result =Api.apiInterface?.serviceReschedule(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSchedulesByProfessionalDetailId(professionalDetailId:Int): Flow<SchedulesByProfessionalDetailIdResponse?> {
        return flow {
            val result =Api.apiInterface?.getSchedulesByProfessionalDetailId(professionalDetailId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addUpdateProfessionalSchedule(request: AddUpdateProfessionalScheduleRequest): Flow<AddUpdateProfessionalScheduleResponse?> {
        return flow {
            val result =Api.apiInterface?.addUpdateProfessionalSchedule(request)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    /*



         suspend fun getSpeciality(): Flow<BrowserServicesResponse?> {
             return flow {
                 val result = Api.apiInterface?.getSpeciality()
                 emit(result)
             }.flowOn(Dispatchers.IO)
         }


         suspend fun getServiceDetail(serviceTypeValue :Int?): Flow<ServiceDetailResponse?> {
             return flow {
                 val result = Api.apiInterface?.getServiceDetail(serviceTypeValue)
                 emit(result)
             }.flowOn(Dispatchers.IO)
         }


         suspend fun getTopDoctors(pageNumber: Int?, pageSize: Int?): Flow<TopDoctorsResponse?> {
             return flow {
                 val result = Api.apiInterface?.getTopDoctors(pageNumber,pageSize)
                 emit(result)
             }.flowOn(Dispatchers.IO)
         }

         suspend fun getSearchTopFindDoctors(pageNumber: Int?, pageSize: Int?,searchQuery: String?): Flow<TopDoctorsResponse?> {
             return flow {
                 val result = Api.apiInterface?.getSearchTopFindDoctors(pageNumber,pageSize,searchQuery)
                 emit(result)
             }.flowOn(Dispatchers.IO)
         }*/

 /*   suspend fun resetPassword(body: ResetPasswordRequest?): Flow<ResetPasswordResponse?> {
        return flow {
            val result = Api.apiInterface?.resetPassword(body)
            emit(result)
        }.flowOn(Dispatchers.IO)
 }
*/
}