package com.medunnes.telemedicine.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medunnes.telemedicine.data.model.Dokter
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.databinding.PasienKonsultasiListBinding

class DokterKonsultasiAdapter(private val dokterList: List<UserAndDokter>): RecyclerView.Adapter<DokterKonsultasiAdapter.ViewHolder>() {
    class ViewHolder(private val binding: PasienKonsultasiListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(dokter: UserAndDokter) {
            with(binding) {
                tvDoctorName.text = dokter.user.fullname
                tvDoctorRole.text= dokter.dokter.tempatPraktik
                tvDoctorExperience.text = dokter.dokter.noStr
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PasienKonsultasiListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dokterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dokterList[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(dokterList[position])
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(dokter: UserAndDokter)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}