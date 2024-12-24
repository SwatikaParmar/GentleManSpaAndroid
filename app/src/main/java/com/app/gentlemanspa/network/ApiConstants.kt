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
    const val RESET_PASSWORD = "Auth/ResetPassword"
    const val GET_PROFESSIONAL_DETAIL = "Professional/GetProfessionalProfileDetail"
    const val UPDATE_PROFESSIONAL = "Professional/UpdateProfessionalProfile"
    const val COUNTRY ="Content/GetCountries"
    const val STATES ="Content/GetStates"
    const val SPECIALITY ="Content/GetAllSpecialities"
    const val CATEGORIES ="Content/GetCategories"
    const val SPA_CATEGORIES ="Admin/GetSpaCategories"
    const val PRODUCT_CATEGORIES ="Product/ProductCategories"
    const val BANNER ="Content/GetBanners"
    const val LOCATION_ADDRESS ="Admin/GetSpas"
    const val SERVICE_LIST ="Service/GetServiceList"
    const val SERVICE_DETAIL ="Service/GetServiceDetail"
    const val PROFESSIONAL_LIST ="Professional/GetProfessionalList"
    const val PROFESSIONAL_SERVICES_LIST ="Service/GetProfessionalServices"
    const val WEEK_DAYS ="Content/GetWeekdays"
    const val PRODUCT_LIST ="Product/GetProductList"
    const val PRODUCT_DETAILS ="Product/GetProductDetails"
    const val ADD_PRODUCT ="Product/AddProduct"
    const val UPDATE_PRODUCT ="Product/UpdateProduct"
    const val UPLOAD_PRODUCT_IMAGE ="Upload/UploadProductImage"
    const val DELETE_PRODUCT ="Product/DeleteProduct"
    const val EMAIL_OTP ="Auth/EmailOTP"
    const val PHONE_UNIQUE ="Auth/IsPhoneUnique"


    const val GET_CUSTOMER_DETAIL = "Customer/GetProfileDetail"
    const val UPDATE_CUSTOMER = "Customer/UpdateProfile"
    const val GET_CUSTOMER_SERVICE_CART_ITEMS ="Customer/GetCartItems"
    const val ADD_CUSTOMER_SERVICE_TO_CART = "Customer/AddUpdateCartService"
    const val GET_CUSTOMER_ADDRESS_LIST = "Customer/GetCustomerAddressList"
    const val ADD_CUSTOMER_ADDRESS = "Customer/AddCustomerAddress"
    const val UPDATE_CUSTOMER_ADDRESS = "Customer/UpdateCustomerAddress"
    const val DELETE_CUSTOMER_ADDRESS= "Customer/DeleteCustomerAddress"
    const val SET_CUSTOMER_ADDRESS_STATUS= "Customer/SetCustomerAddressStatus"
    const val SERVICE_GET_AVAILABLE_DATES = "Service/GetAvailableDates"
    const val SERVICE_GET_AVAILABLE_TIME_SLOTS = "Service/GetAvailableTimeSlots"
    const val ADD_PRODUCT_IN_CART = "Product/AddOrUpdateProductInCart"
    const val CUSTOMER_PLACE_ORDER = "Customer/PlaceOrder"
    const val GET_SERVICE_APPOINTMENTS = "Order/GetServiceAppointments"
    const val CANCEL_UPCOMING_APPOINTMENTS = "Order/CancelOrder"
    const val RESCHEDULE_SERVICE = "Order/RescheduleService"
    const val GET_ORDERED_PRODUCTS = "Order/GetOrderedProducts"
    const val GET_ORDER_DETAIL = "Order/GetOrderDetail"

    const val PROFESSIONAL_SERVICE_LIST ="Service/GetProfessionalServices"
    const val GET_PROFESSIONALS_SCHEDULES_BY_PROFESSIONAL_DETAIL_ID = "Professional/GetProfessionalSchedulesByProfessionalDetailId"
    const val ADD_UPDATE_PROFESSIONAL_SCHEDULE = "Professional/AddUpdateProfessionalSchedule"

    const val GET_EVENT_LIST= "Event/GetEventList"







}