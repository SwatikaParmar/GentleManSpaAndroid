package com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model

import com.google.gson.annotations.SerializedName

data class UpdateProductResponse(

	@field:SerializedName("data")
	val data: UpdateProductData? = null,

	@field:SerializedName("messages")
	val messages: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class UpdateProductData(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("listingPrice")
	val listingPrice: Int? = null,

	@field:SerializedName("productId")
	val productId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("subCategoryId")
	val subCategoryId: Any? = null,

	@field:SerializedName("mainCategoryId")
	val mainCategoryId: Any? = null,

	@field:SerializedName("basePrice")
	val basePrice: Int? = null,

	@field:SerializedName("status")
	val status: Any? = null,

	@field:SerializedName("createDate")
	val createDate: Any? = null,

	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("quantity")
	val quantity: Int? = null
)
