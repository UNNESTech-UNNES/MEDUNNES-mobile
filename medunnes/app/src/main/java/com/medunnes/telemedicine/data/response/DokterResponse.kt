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

	@field:SerializedName("img_dokter")
	val imgDokter: String,

	@field:SerializedName("no_tlp")
	val noTlp: String,

	@field:SerializedName("nama_dokter")
	val namaDokter: String,

	@field:SerializedName("no_reg")
	val noReg: Long,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id_dokter")
	val idDokter: Int,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("tempat_lulus")
	val tempatLulus: String,

	@field:SerializedName("alumni")
	val alumni: String,

	@field:SerializedName("title_depan")
	val titleDepan: String,

	@field:SerializedName("spesialis_id")
	val spesialisId: Int,

	@field:SerializedName("tempat_kerja")
	val tempatKerja: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("title_belakang")
	val titleBelakang: String,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String,

	@field:SerializedName("tgl_mulai_aktif")
	val tglMulaiAktif: String,

	@field:SerializedName("status")
	val status: String
)