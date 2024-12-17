package com.app.gentlemanspa.ui.customerDashboard.fragment.service.model

data class SpaCategoriesResponse(
    val data: List<SpaCategoriesData>,
    val isSuccess: Boolean,
    val messages: String,
    val statusCode: Int
)

data class SpaCategoriesData(
    val categoryDescription: String,
    val categoryId: Int,
    val categoryImage: String,
    val categoryImageFemale: Any,
    val categoryImageIcon: Any,
    val categoryImageMale: Any,
    val categoryName: String,
    val categoryStatus: Boolean,
    val createDate: String,
    val genderType: String,
    val isNext: Boolean,
    val modifyDate: String,
    val spaCategoryId: Int,
    val spaDetailId: Int,
    val status: Boolean,
    val subCategoryId: Int,
    val subSubCategoryId: Int
)