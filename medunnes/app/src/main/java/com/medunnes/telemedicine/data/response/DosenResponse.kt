package com.medunnes.telemedicine.data.response

import com.google.gson.annotations.SerializedName

data class DosenResponse(

	@field:SerializedName("data")
	val data: List<DosenDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class UserDosen(

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
	val emailVerifiedAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String
)

data class DosenDataItem(

	@field:SerializedName("id_dosen")
	val idDosen: Int,

	@field:SerializedName("nip")
	val nip: Long,

	@field:SerializedName("gelar")
	val gelar: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("user")
	val user: User
)
