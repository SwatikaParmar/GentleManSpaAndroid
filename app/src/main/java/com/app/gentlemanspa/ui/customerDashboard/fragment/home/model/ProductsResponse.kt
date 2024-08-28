package com.app.gentlemanspa.ui.customerDashboard.fragment.home.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductsResponse(

	@field:SerializedName("data")
	val data: ProductsData? = null,

	@field:SerializedName("messages")
	val messages: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
) : Parcelable

@Parcelize
data class ProductsListItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("listingPrice")
	val listingPrice: Int? = null,

	@field:SerializedName("productId")
	val productId: Int = 0,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("basePrice")
	val basePrice: Int? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("createDate")
	val createDate: String? = null,

	@field:SerializedName("mainCategoryId")
	val mainCategoryId: Int? = null,

	@field:SerializedName("subCategoryId")
	val subCategoryId: Int? = null,

	@field:SerializedName("stock")
	val stock: Int = 0,

	@field:SerializedName("quantity")
	val quantity: Int? = null

) : Parcelable

@Parcelize
data class ProductsData(

	@field:SerializedName("previousPage")
	val previousPage: Int? = null,

	@field:SerializedName("nextPage")
	val nextPage: Int? = null,

	@field:SerializedName("searchQuery")
	val searchQuery: String? = null,

	@field:SerializedName("dataList")
	val dataList: ArrayList<ProductsListItem>? ,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("pageSize")
	val pageSize: Int? = null,

	@field:SerializedName("totalCount")
	val totalCount: Int? = null,

	@field:SerializedName("currentPage")
	val currentPage: Int? = null
) : Parcelable
