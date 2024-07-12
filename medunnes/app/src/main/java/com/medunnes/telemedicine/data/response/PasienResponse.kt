package com.medunnes.telemedicine.data.response

import com.google.gson.annotations.SerializedName

data class PasienResponse(

	@field:SerializedName("data")
	val data: List<PasienDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class PasienDataItem(

	@field:SerializedName("BB")
	val bB: Int,

	@field:SerializedName("no_tlp")
	val noTlp: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("TB")
	val tB: Int,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("NIK")
	val nik: Long,

	@field:SerializedName("banned_at")
	val bannedAt: Any,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("id_pasien")
	val idPasien: Long,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("img_pasien")
	val imgPasien: String,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String,

	@field:SerializedName("status")
	val status: String
)
