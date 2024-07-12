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
	val user: User,

	@field:SerializedName("status")
	val status: String
)

data class UserPasienDataItem(

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
