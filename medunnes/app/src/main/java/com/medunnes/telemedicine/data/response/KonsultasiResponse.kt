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

data class KonsultasiDataItem(

	@field:SerializedName("topik")
	val topik: String,

	@field:SerializedName("dokter")
	val dokter: Dokter,

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
	val pasien: Pasien,

	@field:SerializedName("status")
	val status: String
)

data class KonsultasiPasien(

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
	val userId: Long,

	@field:SerializedName("id_pasien")
	val idPasien: Long,

	@field:SerializedName("img_pasien")
	val imgPasien: String,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String,

	@field:SerializedName("status")
	val status: String
)

data class Dokter(

	@field:SerializedName("img_dokter")
	val imgDokter: String,

	@field:SerializedName("no_tlp")
	val noTlp: String,

	@field:SerializedName("nama_dokter")
	val namaDokter: String,

	@field:SerializedName("tahun_lulus")
	val tahunLulus: Int,

	@field:SerializedName("no_reg")
	val noReg: Long,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("alumni_kampus")
	val alumniKampus: String,

	@field:SerializedName("id_dokter")
	val idDokter: Long,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("title_depan")
	val titleDepan: String,

	@field:SerializedName("spesialis_id")
	val spesialisId: Int,

	@field:SerializedName("tempat_kerja")
	val tempatKerja: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Long,

	@field:SerializedName("title_belakang")
	val titleBelakang: String,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String,

	@field:SerializedName("tgl_mulai_aktif")
	val tglMulaiAktif: String,

	@field:SerializedName("status")
	val status: String
)
