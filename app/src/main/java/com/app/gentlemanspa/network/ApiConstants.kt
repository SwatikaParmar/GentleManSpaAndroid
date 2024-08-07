package com.app.gentlemanspa.network

object ApiConstants {
    const val BASE = "https://s1jpw3b88a.execute-api.ap-south-1.amazonaws.com/api/"
    const val BASE_FILE = "https://gentlemanspa-file.s3.ap-south-1.amazonaws.com/FileToSave/"
    const val KEY_AUTHORIZATION = "Authorization"



    //Auth

    const val SIGN_UP = "Auth/Register"
    const val PROFILE_REGISTER = "Upload/UploadProfilePic"
    const val SIGN_IN = "Auth/login"
    const val CHANGE_PASSWORD = "Auth/ChangePassword"
    const val GET_PROFESSIONAL_DETAIL = "Professional/GetProfessionalProfileDetail"
    const val UPDATE_PROFESSIONAL = "Professional/UpdateProfessionalProfile"
    const val COUNTRY ="Content/GetCountries"
    const val STATES ="Content/GetStates"
    const val SPECIALITY ="Content/GetAllSpecialities"
    const val CATEGORIES ="Content/GetCategories"
    const val BANNER ="Content/GetBanners"
    const val LOCATION_ADDRESS ="Admin/GetSpas"
    const val SERVICE_LIST ="Service/GetServiceList"
    const val SERVICE_DETAIL ="Service/GetServiceDetail"
    const val PROFESSIONAL_LIST ="Professional/GetProfessionalList"





}