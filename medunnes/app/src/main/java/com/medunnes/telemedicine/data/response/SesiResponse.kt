package com.medunnes.telemedicine.data.response

import com.google.gson.annotations.SerializedName

data class SesiResponse(

	@field:SerializedName("data")
	val data: List<SesiDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class SesiDataItem(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("dari")
	val dari: String,

	@field:SerializedName("day")
	val day: Int,

	@field:SerializedName("sampai")
	val sampai: String,

	@field:SerializedName("id_sesi")
	val idSesi: Int
)
