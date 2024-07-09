package com.medunnes.telemedicine.data.response

import com.google.gson.annotations.SerializedName

data class ArtikelResponse(

	@field:SerializedName("data")
	val data: List<ArtikelDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class ArtikelDataItem(

	@field:SerializedName("judul_artikel")
	val judulArtikel: String,

	@field:SerializedName("desc_artikel")
	val descArtikel: String,

	@field:SerializedName("img_artikel")
	val imgArtikel: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id_artikel")
	val idArtikel: Int
)
