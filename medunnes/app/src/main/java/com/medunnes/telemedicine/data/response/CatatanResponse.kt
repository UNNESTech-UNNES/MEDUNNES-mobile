package com.medunnes.telemedicine.data.response

import com.google.gson.annotations.SerializedName

data class CatatanResponse(

	@field:SerializedName("data")
	val data: List<CatatanDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class CatatanDataItem(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("konsultasi_id")
	val konsultasiId: Int,

	@field:SerializedName("diagnosis")
	val diagnosis: String,

	@field:SerializedName("catatan")
	val catatan: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id_catatan")
	val idCatatan: Int,

	@field:SerializedName("gejala")
	val gejala: String
)
