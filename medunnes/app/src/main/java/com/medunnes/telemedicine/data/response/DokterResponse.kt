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

data class UserDokterDataItem(

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

data class DokterDataItem(

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
	val user: User,

	@field:SerializedName("status")
	val status: String
)
