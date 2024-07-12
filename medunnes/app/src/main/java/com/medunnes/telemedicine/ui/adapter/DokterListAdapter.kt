package com.medunnes.telemedicine.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.databinding.ListPasienJanjiBinding
import com.medunnes.telemedicine.utils.imageBaseUrl

class DokterListAdapter(private val dokterList: ArrayList<DokterDataItem>) : RecyclerView.Adapter<DokterListAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: ListPasienJanjiBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dokter: DokterDataItem) {
            with(binding) {
                val spesialis = root.resources.getStringArray(R.array.spesialissasi)
                tvDoctorRole.text = spesialis[(dokter.spesialisId.toInt())-1]

                if (!dokter.imgDokter.isNullOrEmpty()) {
                    val imageFile = "${imageBaseUrl()}/${dokter.imgDokter}"
                    Glide.with(itemView.context)
                        .load(imageFile)
                        .into(ivMessanger)
                        .clearOnDetach()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListPasienJanjiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = dokterList.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(dokterList[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(dokterList[position])
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(dokter: DokterDataItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}