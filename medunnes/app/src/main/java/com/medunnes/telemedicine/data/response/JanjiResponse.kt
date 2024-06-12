package com.medunnes.telemedicine.data.response

import com.google.gson.annotations.SerializedName

data class JanjiResponse(

	@field:SerializedName("data")
	val data: List<JanjiDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class JanjiDataItem(

	@field:SerializedName("datetime")
	val datetime: String,

	@field:SerializedName("pasien_id")
	val pasienId: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("dokter_id")
	val dokterId: Int,

	@field:SerializedName("id_janji")
	val idJanji: Int,

	@field:SerializedName("sesi_id")
	val sesiId: Int,

	@field:SerializedName("catatan")
	val catatan: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("pasien_tambahan_id")
	val pasienTambahanId: Int,

	@field:SerializedName("status")
	val status: String
)
