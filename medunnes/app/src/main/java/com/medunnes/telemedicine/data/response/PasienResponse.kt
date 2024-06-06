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

	@field:SerializedName("nama_pasien")
	val namaPasien: String,

	@field:SerializedName("no_tlp")
	val noTlp: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("TB")
	val tB: Int,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("nik")
	val nik: Long,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("id_pasien")
	val idPasien: Int,

	@field:SerializedName("img_pasien")
	val imgPasien: String,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String,

	@field:SerializedName("status")
	val status: String
)
