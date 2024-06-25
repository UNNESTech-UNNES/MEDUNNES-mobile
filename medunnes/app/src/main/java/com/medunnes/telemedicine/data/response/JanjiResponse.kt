package com.medunnes.telemedicine.data.response

import com.google.gson.annotations.SerializedName

data class JanjiResponse(

	@field:SerializedName("data")
	val data: List<JanjiDataItem>,

	@field:SerializedName("notification")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class JanjiDataItem(

	@field:SerializedName("datetime")
	val datetime: String,

	@field:SerializedName("pasien_tambahan")
	val pasienTambahan: PasienTambahan,

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

	@field:SerializedName("pasien")
	val pasien: Pasien,

	@field:SerializedName("pasien_tambahan_id")
	val pasienTambahanId: Int,

	@field:SerializedName("status")
	val status: String
)

data class Pasien(

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

	@field:SerializedName("NIK")
	val nIK: Long,

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

data class PasienTambahan(

	@field:SerializedName("BB")
	val bB: Int,

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
	val tB: Int,

	@field:SerializedName("tgl_lahir")
	val tglLahir: String
)
