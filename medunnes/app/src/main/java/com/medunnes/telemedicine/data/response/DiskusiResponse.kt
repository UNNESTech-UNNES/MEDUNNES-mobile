package com.medunnes.telemedicine.data.response

import com.google.gson.annotations.SerializedName

data class DiskusiResponse(

	@field:SerializedName("data")
	val data: List<DiskusiDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class DiskusiDataItem(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("konsultasi_id")
	val konsultasiId: Long,

	@field:SerializedName("id_diskusi")
	val idDiskusi: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("message")
	val message: String
)
