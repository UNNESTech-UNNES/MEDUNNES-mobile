package com.medunnes.telemedicine.data.response

import com.google.gson.annotations.SerializedName

data class PasienTambahanResponse(

	@field:SerializedName("data")
	val data: List<PasienTambahanDataItem>,

	@field:SerializedName("notification")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class PasienTambahanDataItem(

	@field:SerializedName("BB")
	val bB: String,

	@field:SerializedName("hubungan_keluarga")
	val hubunganKeluarga: String,

	@field:SerializedName("pasien_id")
	val pasienId: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("nama_pasien_tambahan")
	val namaPasienTambahan: String,

	@field:SerializedName("id_pasien_tambahan")
	val idPasienTambahan: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String,

	@field:SerializedName("TB")
	val tB: String,

	@field:SerializedName("tgl_lahir")
	val tglLahir: String
)
