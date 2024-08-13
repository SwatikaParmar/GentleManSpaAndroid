package com.app.gentlemanspa.network

import com.app.gentlemanspa.ui.auth.fragment.login.model.LoginRequest
import com.app.gentlemanspa.ui.auth.fragment.login.model.LoginResponse
import com.app.gentlemanspa.ui.auth.fragment.register.model.ProfileRegisterResponse
import com.app.gentlemanspa.ui.auth.fragment.register.model.SignUpRequest
import com.app.gentlemanspa.ui.auth.fragment.register.model.SignUpResponse
import com.app.gentlemanspa.ui.auth.fragment.setPassword.model.ChangePasswordRequest
import com.app.gentlemanspa.ui.auth.fragment.setPassword.model.ChangePasswordResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.BannerResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.LocationResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model.SpecialityResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.profile.model.GetProfessionalDetailResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model.UpdateProfileProfessionalRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.editProfile.model.UpdateProfileProfessionalResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.CategoriesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductCategoriesResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.home.model.ProductsResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.productDetail.model.ProductDetailResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.selectProfessional.model.ProfessionalResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.service.model.ServiceResponse
import com.app.gentlemanspa.ui.customerDashboard.fragment.serviceDetail.model.ServiceDetailResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.schedule.model.WeekDaysResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.model.country.CountryResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.selectCountry.model.states.StatesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
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


    @GET(ApiConstants.GET_PROFESSIONAL_DETAIL)
    suspend fun getProfessionalDetail(): GetProfessionalDetailResponse

    @POST(ApiConstants.UPDATE_PROFESSIONAL)
    suspend fun updateProfessional(@Body body: UpdateProfileProfessionalRequest?): UpdateProfileProfessionalResponse

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

    @GET(ApiConstants.BANNER)
    suspend fun getBanner(): BannerResponse


    @GET(ApiConstants.LOCATION_ADDRESS)
    suspend fun getLocationAddress(): LocationResponse


    @GET(ApiConstants.LOCATION_ADDRESS)
    suspend fun getSearchLocationAddress(@Query("searchQuery") search: String?): LocationResponse


    @GET(ApiConstants.SERVICE_LIST)
    suspend fun getServiceList(@Query("pageNumber") pageNumber: Int?,@Query("pageSize") pageSize: Int?,@Query("categoryId") categoryId: Int?): ServiceResponse


    @GET(ApiConstants.SERVICE_DETAIL)
    suspend fun getServiceDetail(@Query("serviceId") serviceId: Int?,@Query("spaDetailId") spaDetailId: Int?): ServiceDetailResponse

    @GET(ApiConstants.PROFESSIONAL_LIST)
    suspend fun getProfessionalList(@Query("serviceId") serviceId: Int?,@Query("spaDetailId") spaDetailId: Int?): ProfessionalResponse

    @GET(ApiConstants.WEEK_DAYS)
    suspend fun getWeekDays(): WeekDaysResponse


    @GET(ApiConstants.PRODUCT_LIST)
    suspend fun getProductsList(@Query("PageNumber") pageNumber: Int?,
                                @Query("PageSize") pageSize: Int?,
                                @Query("MainCategoryId") mainCategoryId: Int?
    ): ProductsResponse

    @GET(ApiConstants.PRODUCT_DETAILS)
    suspend fun getProductDetails(@Query("id") id: Int?): ProductDetailResponse
    /*

        @POST(ApiConstants.EMAIL_OTP)
        suspend fun emailOtp(
            @Body body: EmailOtpRequest?
        ): EmailOtpResponse



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