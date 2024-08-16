package com.app.gentlemanspa.ui.professionalDashboard.fragment.addProduct.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateProductRequest(

	@field:SerializedName("listingPrice")
	val listingPrice: Int? = null,

	@field:SerializedName("productId")
	val productId: Int? = null,

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
	val basePrice: Int? = null
) : Parcelable
