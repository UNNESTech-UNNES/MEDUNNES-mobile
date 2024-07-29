package com.medunnes.telemedicine.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("status")
	val status: Boolean
)

data class User(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("created_token")
	val createdToken: Any,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: Any,

	@field:SerializedName("id")
	val idUser: Int,

	@field:SerializedName("role")
	val type: String,

	@field:SerializedName("email")
	val email: String
)
