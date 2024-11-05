package com.app.gentlemanspa.network

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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiInterface {

    @POST(ApiConstants.SIGN_UP)
    suspend fun registerAccount(
        @Body body: SignUpRequest?
    ): SignUpResponse

    @Multipart
    @POST(ApiConstants.PROFILE_REGISTER)
    suspend fun profilePicRegister(
        @Part profilePic: MultipartBody.Part?,
        @Part("id") id: RequestBody?
    ): ProfileRegisterResponse


    @POST(ApiConstants.SIGN_IN)
    suspend fun loginAccount(
        @Body body: LoginRequest?
    ): LoginResponse


    @POST(ApiConstants.CHANGE_PASSWORD)
    suspend fun setChangePassword(
        @Body body: ChangePasswordRequest?
    ): ChangePasswordResponse

    @POST(ApiConstants.RESET_PASSWORD)
    suspend fun resetPassword(
        @Body body: ForgetPasswordRequest?
    ): ForgetPasswordResponse

    @GET(ApiConstants.GET_PROFESSIONAL_DETAIL)
    suspend fun getProfessionalDetail(): GetProfessionalDetailResponse

    @POST(ApiConstants.UPDATE_PROFESSIONAL)
    suspend fun updateProfessional(@Body body: UpdateProfileProfessionalRequest?): UpdateProfileProfessionalResponse

    @GET(ApiConstants.GET_CUSTOMER_DETAIL)
    suspend fun getCustomerDetail(): GetProfessionalDetailResponse

    @POST(ApiConstants.UPDATE_CUSTOMER)
    suspend fun updateCustomerProfile(@Body body: UpdateProfileCustomerRequest?): UpdateProfileCustomerResponse

    @GET(ApiConstants.COUNTRY)
    suspend fun getCountry(
        @Query("searchQuery") search: String?
    ): CountryResponse

    @GET(ApiConstants.STATES)
    suspend fun getStates(
        @Query("countryId") countryId: Int?,
        @Query("searchQuery") search: String?
    ): StatesResponse


    @GET(ApiConstants.SPECIALITY)
    suspend fun getSpeciality(): SpecialityResponse

    @GET(ApiConstants.CATEGORIES)
    suspend fun getCategories(): CategoriesResponse

    @GET(ApiConstants.PRODUCT_CATEGORIES)
    suspend fun getProductCategories(): ProductCategoriesResponse

    @GET(ApiConstants.GET_CUSTOMER_SERVICE_CART_ITEMS)
    suspend fun getServiceCartItems(): GetCartItemsResponse

    @GET(ApiConstants.GET_CUSTOMER_ADDRESS_LIST)
    suspend fun geCustomerAddressList(): CustomerAddressResponse

    @POST(ApiConstants.ADD_CUSTOMER_ADDRESS)
    suspend fun addCustomerAddress(
        @Body body: AddCustomerAddressRequest?
    ): AddCustomerAddressResponse

    @POST(ApiConstants.UPDATE_CUSTOMER_ADDRESS)
    suspend fun updateCustomerAddress(
        @Body body: AddCustomerAddressRequest?
    ): AddCustomerAddressResponse

    @POST(ApiConstants.SET_CUSTOMER_ADDRESS_STATUS)
    suspend fun setCustomerAddressStatus(
        @Body body: CustomerAddressStatusRequest?
    ): CustomerAddressStatusResponse

    @DELETE(ApiConstants.DELETE_CUSTOMER_ADDRESS)
    suspend fun deleteCustomerAddress(
        @Query("customerAddressId") customerAddressId: Int?,
    ): DeleteAddressResponse

    @POST(ApiConstants.ADD_CUSTOMER_SERVICE_TO_CART)
    suspend fun addServiceToCart(
        @Body body: AddServiceToCartRequest?
    ): AddServiceToCartResponse

    @POST(ApiConstants.CUSTOMER_PLACE_ORDER)
    suspend fun customerPlaceOrder(
        @Body body: CustomerPlaceOrderRequest?
    ): CustomerPlaceOrderResponse

    @POST(ApiConstants.RESCHEDULE_SERVICE)
    suspend fun serviceReschedule(
        @Body body: ServiceRescheduleRequest?
    ): ServiceRescheduleResponse

    @POST(ApiConstants.ADD_PRODUCT_IN_CART)
    suspend fun addProductInCart(
        @Body body: AddProductInCartRequest?
    ): AddProductInCartResponse

    @GET(ApiConstants.BANNER)
    suspend fun getBanner(): BannerResponse


    @GET(ApiConstants.LOCATION_ADDRESS)
    suspend fun getLocationAddress(): LocationResponse


    @GET(ApiConstants.LOCATION_ADDRESS)
    suspend fun getSearchLocationAddress(@Query("searchQuery") search: String?): LocationResponse


    @GET(ApiConstants.SERVICE_LIST)
    suspend fun getServiceList(
        @Query("pageNumber") pageNumber: Int?,
        @Query("pageSize") pageSize: Int?,
        @Query("categoryId") categoryId: Int?,
        @Query("SearchQuery") searchQuery: String?,
        @Query("spaDetailId") spaDetailId: Int?
    ): ServiceResponse

    @GET(ApiConstants.PROFESSIONAL_SERVICES_LIST)
    suspend fun getProfessionalServiceList(
        @Query("professionalDetailId") professionalDetailId: Int?
    ): ProfessionalServicesResponse


    @GET(ApiConstants.SERVICE_DETAIL)
    suspend fun getServiceDetail(
        @Query("serviceId") serviceId: Int?,
        @Query("spaDetailId") spaDetailId: Int?
    ): ServiceDetailResponse

    @GET(ApiConstants.SERVICE_GET_AVAILABLE_DATES)
    suspend fun getServiceAvailableDates(
        @Query("SpaServiceIds") spaServiceIds: Int?,
        @Query("ProfessionalId") professionalId: String?
    ): ServiceGetAvailableDatesResponse

    @GET(ApiConstants.SERVICE_GET_AVAILABLE_TIME_SLOTS)
    suspend fun getServiceAvailableTimeSlots(
        @Query("SpaServiceIds") spaServiceIds: Int?,
        @Query("Date") date: String?,
        @Query("ProfessionalId") professionalId: String?
    ): ServiceGetAvailableTimeSlotsResponse

    @GET(ApiConstants.PROFESSIONAL_LIST)
    suspend fun getProfessionalList(
        @Query("spaServiceId") spaServiceId: Int?,
        @Query("spaDetailId") spaDetailId: Int?
    ): ProfessionalResponse

    @GET(ApiConstants.PROFESSIONAL_LIST)
    suspend fun getProfessionalTeamList(
    ): ProfessionalResponse

    @GET(ApiConstants.WEEK_DAYS)
    suspend fun getWeekDays(): WeekDaysResponse


    @GET(ApiConstants.PRODUCT_LIST)
    suspend fun getProductsList(
        @Query("PageNumber") pageNumber: Int?,
        @Query("PageSize") pageSize: Int?,
        @Query("MainCategoryId") mainCategoryId: Int?,
        @Query("SearchQuery") searchQuery: String?,
        @Query("SpaDetailId") spaDetailId: Int?
    ): ProductsResponse

    @GET(ApiConstants.PRODUCT_DETAILS)
    suspend fun getProductDetails(@Query("id") id: Int?): ProductDetailResponse


    @POST(ApiConstants.ADD_PRODUCT)
    suspend fun addProduct(
        @Body body: AddProductRequest?
    ): AddProductResponse


    @POST(ApiConstants.UPDATE_PRODUCT)
    suspend fun updateProduct(
        @Body body: UpdateProductRequest?
    ): UpdateProductResponse


    @Multipart
    @POST(ApiConstants.UPLOAD_PRODUCT_IMAGE)
    suspend fun uploadProductImage(
        @Part("ProductId") productId: RequestBody?, @Part images: ArrayList<MultipartBody.Part>?
    ): UploadProductImageResponse

    @DELETE(ApiConstants.DELETE_PRODUCT)
    suspend fun getDeleteProduct(@Query("id") id: Int?): DeleteProductResponse


    @POST(ApiConstants.EMAIL_OTP)
    suspend fun emailOtp(
        @Body body: EmailOtpRequest?
    ): EmailOtpResponse

    @POST(ApiConstants.PHONE_UNIQUE)
    suspend fun phoneUnique(
        @Body body: PhoneUniqueRequest?
    ): EmailOtpResponse

    @GET(ApiConstants.GET_SERVICE_APPOINTMENTS)
    suspend fun getServiceAppointments(
        @Query("type") type: String?,
        @Query("pageSize") pageSize: Int?,
        @Query("pageNumber") pageNumber: Int?
    ): UpcomingServiceAppointmentResponse

    @POST(ApiConstants.CANCEL_UPCOMING_APPOINTMENTS)
    suspend fun cancelUpcomingAppointment(
       @Body request:CancelUpcomingAppointmentRequest
    ): CancelUpcomingAppointmentResponse

    @GET(ApiConstants.GET_ORDERED_PRODUCTS)
    suspend fun getOrderedProducts(
        @Query("type") type: String?,
        @Query("pageSize") pageSize: Int?,
        @Query("pageNumber") pageNumber: Int?
    ): MyOrdersResponse

    @GET(ApiConstants.GET_PROFESSIONALS_SCHEDULES_BY_PROFESSIONAL_DETAIL_ID)
    suspend fun getSchedulesByProfessionalDetailId(
        @Query("professionalDetailId")professionalDetailId:Int): SchedulesByProfessionalDetailIdResponse

    @POST(ApiConstants.ADD_UPDATE_PROFESSIONAL_SCHEDULE)
    suspend fun addUpdateProfessionalSchedule(
    @Body request:AddUpdateProfessionalScheduleRequest ): AddUpdateProfessionalScheduleResponse

    /*
       @GET(ApiConstants.SPECIALITY)
       suspend fun getSpeciality(
       ): BrowserServicesResponse

       @GET(ApiConstants.SERVICE_DETAIL)
       suspend fun getServiceDetail(@Query("serviceTypeValue") serviceTypeValue: Int?
       ): ServiceDetailResponse

       @GET(ApiConstants.TOP_DOCTORS)
       suspend fun getTopDoctors(@Query("pageNumber") pageNumber: Int?,
                                 @Query("pageSize") pageSize: Int?
       ): TopDoctorsResponse

       @GET(ApiConstants.TOP_DOCTORS)
       suspend fun getSearchTopFindDoctors( @Query("pageNumber") pageNumber: Int?,
                                            @Query("pageSize") pageSize: Int?,  @Query("searchQuery") searchQuery: String?): TopDoctorsResponse
   */

    /*  @POST(ApiConstants.RESET_PASSWORD)
      suspend fun resetPassword(
          @Body body: ResetPasswordRequest?
      ): ResetPasswordResponse*/


}