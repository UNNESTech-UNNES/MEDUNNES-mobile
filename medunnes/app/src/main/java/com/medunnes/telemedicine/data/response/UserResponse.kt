package com.medunnes.telemedicine.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("notification")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class DataItem(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("created_token")
	val createdToken: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: Any? = null,

	@field:SerializedName("id_user")
	val idUser: Int,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("email")
	val email: String
)
