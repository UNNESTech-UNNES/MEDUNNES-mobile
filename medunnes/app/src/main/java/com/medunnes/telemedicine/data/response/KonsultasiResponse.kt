package com.medunnes.telemedicine.data.response

import com.google.gson.annotations.SerializedName

data class KonsultasiResponse(

	@field:SerializedName("data")
	val data: List<KonsultasiDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class PasienKonsultasiDataItem(

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
	val nik: Int,

	@field:SerializedName("banned_at")
	val bannedAt: Any,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("id_pasien")
	val idPasien: Long,

	@field:SerializedName("id")
	val id: Long,

	@field:SerializedName("img_pasien")
	val imgPasien: String,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String,

	@field:SerializedName("user")
	val user: UserKonsultasiDataItem,

	@field:SerializedName("status")
	val status: String
)

data class UserKonsultasiDataItem(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: Any,

	@field:SerializedName("id")
	val id: Long,

	@field:SerializedName("email")
	val email: String
)

data class DokterKonsultasiDataItem(

	@field:SerializedName("dosen_id")
	val dosenId: Long,

	@field:SerializedName("img_dokter")
	val imgDokter: String,

	@field:SerializedName("no_tlp")
	val noTlp: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id_dokter")
	val idDokter: Long,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("nim")
	val nim: Long,

	@field:SerializedName("spesialis_id")
	val spesialisId: Long,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("id")
	val id: Long,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String,

	@field:SerializedName("user")
	val user: UserKonsultasiDataItem,

	@field:SerializedName("status")
	val status: String
)

data class KonsultasiDataItem(

	@field:SerializedName("topik")
	val topik: String,

	@field:SerializedName("dokter")
	val dokter: DokterKonsultasiDataItem,

	@field:SerializedName("pasien_id")
	val pasienId: Long,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("dokter_id")
	val dokterId: Long,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id_konsultasi")
	val idKonsultasi: Long,

	@field:SerializedName("pasien")
	val pasien: PasienKonsultasiDataItem,

	@field:SerializedName("status")
	val status: String
)
