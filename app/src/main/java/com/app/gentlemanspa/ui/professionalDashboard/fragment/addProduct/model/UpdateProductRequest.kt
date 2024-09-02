package com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateProductRequest(

	@field:SerializedName("listingPrice")
	val listingPrice: Double? = 0.00,

	@field:SerializedName("productId")
	val productId: Int? = 0,

	@field:SerializedName("name")
	val name: String? = "",

	@field:SerializedName("description")
	val description: String? = "",

	@field:SerializedName("subCategoryId")
	val subCategoryId: Int? = 0,

	@field:SerializedName("spaDetailId")
	val spaDetailId: Int? = 0,

	@field:SerializedName("mainCategoryId")
	val mainCategoryId: Int? = 0,

	@field:SerializedName("basePrice")
	val basePrice: Double? = 0.00,

	@field:SerializedName("stock")
	val stock: Int? = 0,

	@field:SerializedName("quantity")
	val quantity: Int? = 0
) : Parcelable
