package com.medunnes.telemedicine.ui.adapter

import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.data.model.Dokter
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.databinding.KonsultasiPasienListBinding
import com.medunnes.telemedicine.databinding.PasienKonsultasiListBinding
import java.io.File

class DokterKonsultasiAdapter(private val dokterList: List<UserAndDokter>): RecyclerView.Adapter<DokterKonsultasiAdapter.ViewHolder>() {
    class ViewHolder(private val binding: KonsultasiPasienListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(dokter: UserAndDokter) {
            with(binding) {
                tvDokterNama.text = "${dokter.dokter.titelDepan} ${dokter.user.fullname} ${dokter.dokter.titelBelakang}"
                tvDokterSpesialis.text= dokter.dokter.spesialidId.toString()

//                if (!dokter.dokter..isNullOrEmpty()) {
//                    val path = Environment.getExternalStorageDirectory()
//                    val imageFile = "${File(path, "/Android/data/com.medunnes.telemedicine${dokter.user.image}")}"
//                    Glide.with(itemView.context)
//                        .load(imageFile)
//                        .into(ivDokterPicture)
//                        .clearOnDetach()
//                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = KonsultasiPasienListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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