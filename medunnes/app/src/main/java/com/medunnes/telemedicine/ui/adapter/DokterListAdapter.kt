package com.medunnes.telemedicine.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.databinding.PasienJanjiListBinding

class DokterListAdapter(private val dokterList: ArrayList<UserAndDokter>) : RecyclerView.Adapter<DokterListAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: PasienJanjiListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dokter: UserAndDokter) {
            with(binding) {
                tvPatientName.text = "${dokter.dokter.titelDepan} ${dokter.user.fullname} ${dokter.dokter.titelBelakang}"
                tvDoctorRole.text = dokter.dokter.tempatPraktik
                tvDoctorExperience.text = dokter.user.tanggalLahir
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = PasienJanjiListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = dokterList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(dokterList[position])
    }
}