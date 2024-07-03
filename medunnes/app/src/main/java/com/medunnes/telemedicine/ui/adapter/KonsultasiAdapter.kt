package com.medunnes.telemedicine.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.data.model.Messanger
import com.medunnes.telemedicine.data.response.KonsultasiDataItem
import com.medunnes.telemedicine.databinding.ListKonsultasiBinding
import com.medunnes.telemedicine.utils.imageBaseUrl

class KonsultasiAdapter(private val konsultasiList: ArrayList<KonsultasiDataItem>) : RecyclerView.Adapter<KonsultasiAdapter.ListViewHolder>(){
    class ListViewHolder(private val binding: ListKonsultasiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(konsultasi: KonsultasiDataItem) {
            with(binding) {
                tvPatientName.text = konsultasi.pasien.namaPasien
                tvPatientSession.text = konsultasi.topik
                tvPatientStatus.text = if (konsultasi.pasien.status == "berlangsung") "Berlangsung" else "Berakhir"

                if (!konsultasi.pasien.imgPasien.isNullOrEmpty()) {
                    val imagePath = "${imageBaseUrl()}/${konsultasi.pasien.imgPasien}"
                    Glide.with(itemView.context)
                        .load(imagePath)
                        .into(binding.ivMessanger)
                        .clearOnDetach()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): ListViewHolder {
        val binding = ListKonsultasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        fun onItemClicked(konsultasi: KonsultasiDataItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}