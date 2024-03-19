package com.medunnes.telemedicine.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.data.model.Konsultasi
import com.medunnes.telemedicine.data.model.Messanger
import com.medunnes.telemedicine.databinding.KonsultasiListBinding

class KonsultasiAdapter(private val konsultasiList: ArrayList<Konsultasi>) : RecyclerView.Adapter<KonsultasiAdapter.ListViewHolder>(){
    class ListViewHolder(private val binding: KonsultasiListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(konsultasi: Konsultasi) {
            with(binding) {
                tvPatientName.text = konsultasi.namaPatient
                tvPatientSession.text = konsultasi.sesiPatient
                tvPatientStatus.text = konsultasi.statusPatient
                Glide.with(itemView.context)
                    .load(konsultasi.fotoPatient)
                    .into(ivMessanger)
                    .clearOnDetach()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): ListViewHolder {
        val binding = KonsultasiListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(konsultasiList[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(konsultasiList[position])
        }
    }

    override fun getItemCount(): Int {
        return konsultasiList.size
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(konsultasi: Konsultasi)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}