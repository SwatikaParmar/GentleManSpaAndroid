package com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddProductRequest(

	@field:SerializedName("listingPrice")
	val listingPrice: Int? = null,

	@field:SerializedName("createdBy")
	val createdBy: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("subCategoryId")
	val subCategoryId: Int? = null,

	@field:SerializedName("spaDetailId")
	val spaDetailId: Int? = null,

	@field:SerializedName("mainCategoryId")
	val mainCategoryId: Int? = null,

	@field:SerializedName("basePrice")
	val basePrice: Int? = null,

	@field:SerializedName("stock")
	val stock: Int? = null,

	@field:SerializedName("quantity")
	val quantity: Int? = null
) : Parcelable
