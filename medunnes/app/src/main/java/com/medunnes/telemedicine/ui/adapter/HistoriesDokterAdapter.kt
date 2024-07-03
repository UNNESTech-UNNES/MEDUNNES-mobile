package com.medunnes.telemedicine.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.response.KonsultasiDataItem
import com.medunnes.telemedicine.databinding.ListHistoriesBinding
import com.medunnes.telemedicine.utils.imageBaseUrl

class HistoriesDokterAdapter(private val dokterList: List<KonsultasiDataItem>): RecyclerView.Adapter<HistoriesDokterAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ListHistoriesBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(dokter: KonsultasiDataItem) {
            with(binding) {
                tvDokterNama.text = dokter.pasien.namaPasien
                tvDokterSpesialis.visibility = View.INVISIBLE
                tvDokterSesiWaktu.text = if (dokter.status == "berlangsung") "Berlangsung" else "Berakhir"

                val imgDokter = dokter.pasien.imgPasien
                if (!imgDokter.isNullOrEmpty()) {
                    val imagePath = "${imageBaseUrl()}/${imgDokter}"
                    Glide.with(itemView.context)
                        .load(imagePath)
                        .into(ivDokterPicture)
                        .clearOnDetach()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListHistoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        fun onItemClicked(konsultasi: KonsultasiDataItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}