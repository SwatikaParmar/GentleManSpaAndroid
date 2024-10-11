package com.app.gentlemanspa.ui.customerDashboard.fragment.home.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Parcelize
data class ProductsResponse(

	@field:SerializedName("data")
	val data: ProductsData?,

	@field:SerializedName("messages")
	val messages: String? = "",

	@field:SerializedName("statusCode")
	val statusCode: Int? = 0,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = false
) : Parcelable

@Parcelize
data class ProductsListItem(

	@field:SerializedName("image")
	val image: String? = "",

	@field:SerializedName("listingPrice")
	val listingPrice: Double? = 0.0,

	@field:SerializedName("productId")
	val productId: Int = 0,


	@field:SerializedName("countInCart")
	val countInCart : Int = 0,

	@field:SerializedName("name")
	val name: String? = "",

	@field:SerializedName("description")
	val description: String? = "",

	@field:SerializedName("basePrice")
	val basePrice: Double? = 0.0,

	@field:SerializedName("status")
	val status: String? = "",

	@field:SerializedName("createDate")
	val createDate: String? = "",

	@field:SerializedName("mainCategoryId")
	val mainCategoryId: Int? = 0,

	@field:SerializedName("subCategoryId")
	val subCategoryId: Int? = 0,

	@field:SerializedName("stock")
	val stock: Int = 0,

	@field:SerializedName("quantity")
	val quantity: Int? = 0

) : Parcelable

@Parcelize
data class ProductsData(

	@field:SerializedName("previousPage")
	val previousPage: Int? = 0,

	@field:SerializedName("nextPage")
	val nextPage: Int? = 0,

	@field:SerializedName("searchQuery")
	val searchQuery: String? = "",

	@field:SerializedName("dataList")
	val dataList: ArrayList<ProductsListItem>? ,

	@field:SerializedName("totalPages")
	val totalPages: Int? = 0,

	@field:SerializedName("pageSize")
	val pageSize: Int? = 0,

	@field:SerializedName("totalCount")
	val totalCount: Int? = 0,

	@field:SerializedName("currentPage")
	val currentPage: Int? = 0
) : Parcelable
