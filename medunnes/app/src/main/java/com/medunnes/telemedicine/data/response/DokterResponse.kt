package com.medunnes.telemedicine.data.response

import com.google.gson.annotations.SerializedName

data class DokterResponse(

	@field:SerializedName("data")
	val data: List<DokterDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class DokterDataItem(

	@field:SerializedName("dosen_id")
	val dosenId: Long,

	@field:SerializedName("img_dokter")
	val imgDokter: String,

	@field:SerializedName("nim")
	val nim: Long,

	@field:SerializedName("spesialis_id")
	val spesialisId: Long,

	@field:SerializedName("no_tlp")
	val noTlp: String,

	@field:SerializedName("updated_at")
	val updatedAt: Any,

	@field:SerializedName("created_at")
	val createdAt: Any,

	@field:SerializedName("id")
	val id: Long,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String,

	@field:SerializedName("id_dokter")
	val idDokter: Long,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("status")
	val status: String
)
