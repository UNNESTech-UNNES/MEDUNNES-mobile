package com.medunnes.telemedicine.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.response.KonsultasiDataItem
import com.medunnes.telemedicine.databinding.ListKonsultasiPasienBinding
import com.medunnes.telemedicine.utils.imageBaseUrl

class DokterKonsultasiAdapter(private val dokterList: List<KonsultasiDataItem>): RecyclerView.Adapter<DokterKonsultasiAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ListKonsultasiPasienBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(konsultasi: KonsultasiDataItem) {
            with(binding) {

                val spesialis = root.resources.getStringArray(R.array.spesialissasi)
                val spesiliasId = konsultasi.dokter.spesialisId.toInt()
                tvDokterNama.text = konsultasi.dokter.user.name
                tvDokterSpesialis.text = spesialis[spesiliasId-1]
                tvDokterSesiWaktu.text = konsultasi.status

                val imgDokter = konsultasi.dokter.imgDokter
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
        val binding = ListKonsultasiPasienBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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