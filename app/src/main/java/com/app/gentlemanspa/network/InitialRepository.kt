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
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.AddProductRequest
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.AddProductResponse
import com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model.UploadProductImageResponse
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
import retrofit2.http.Query


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


    suspend fun getServiceList(pageNumber: Int?, pageSize: Int?,categoryId: Int?): Flow<ServiceResponse?> {
        return flow {
            val result =Api.apiInterface?.getServiceList(pageNumber,pageSize,categoryId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getServiceDetail(serviceId: Int?,spaDetailId: Int?): Flow<ServiceDetailResponse?> {
        return flow {
            val result =Api.apiInterface?.getServiceDetail(serviceId,spaDetailId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProfessionalList(serviceId: Int?,spaDetailId: Int?): Flow<ProfessionalResponse?> {
        return flow {
            val result =Api.apiInterface?.getProfessionalList(serviceId,spaDetailId)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getWeekDays(): Flow<WeekDaysResponse?> {
        return flow {
            val result =Api.apiInterface?.getWeekDays()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getProductsList(pageNumber: Int?,pageSize: Int?,mainCategoryId: Int?): Flow<ProductsResponse?> {
        return flow {
            val result =Api.apiInterface?.getProductsList(pageNumber,pageSize,mainCategoryId)
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

    suspend fun uploadProductImage(projectId : RequestBody?, productImages: ArrayList<MultipartBody.Part>?):Flow<UploadProductImageResponse?>{
        return flow {
            val result =Api.apiInterface?.uploadProductImage(projectId,productImages)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
    /*
     suspend fun emailOtp(body: EmailOtpRequest?): Flow<EmailOtpResponse?> {
         return flow {
             val result = Api.apiInterface?.emailOtp(body)
             emit(result)
         }.flowOn(Dispatchers.IO)
     }






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