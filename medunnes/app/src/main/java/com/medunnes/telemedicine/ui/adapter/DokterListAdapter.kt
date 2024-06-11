package com.medunnes.telemedicine.ui.adapter

import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.databinding.PasienJanjiListBinding
import java.io.File

class DokterListAdapter(private val dokterList: ArrayList<DokterDataItem>) : RecyclerView.Adapter<DokterListAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: PasienJanjiListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dokter: DokterDataItem) {
            with(binding) {
                tvPatientName.text = "${dokter.titleDepan} ${dokter.namaDokter} ${dokter.titleBelakang}"

                val spesialis = root.resources.getStringArray(R.array.spesialissasi)
                tvDoctorRole.text = spesialis[(dokter.spesialisId.toInt())-1]
                tvDoctorExperience.text = dokter.tempatKerja

//                if (!dokter.user.isNullOrEmpty()) {
//                    val path = Environment.getExternalStorageDirectory()
//                    val imageFile = "${File(path, "/Android/data/com.medunnes.telemedicine${dokter.user.image}")}"
//                    Glide.with(itemView.context)
//                        .load(imageFile)
//                        .into(ivMessanger)
//                        .clearOnDetach()
//                }
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