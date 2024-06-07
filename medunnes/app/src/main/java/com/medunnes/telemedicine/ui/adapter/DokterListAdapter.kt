package com.medunnes.telemedicine.ui.adapter

import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.data.model.Faskes
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.databinding.PasienJanjiListBinding
import java.io.File

class DokterListAdapter(private val dokterList: ArrayList<UserAndDokter>) : RecyclerView.Adapter<DokterListAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: PasienJanjiListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dokter: UserAndDokter) {
            with(binding) {
                tvPatientName.text = "${dokter.dokter.titelDepan} ${dokter.user.fullname} ${dokter.dokter.titelBelakang}"
                //tvDoctorRole.text = dokter.dokter.spesialis
                tvDoctorExperience.text = dokter.user.fullname

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
        fun onItemClicked(dokter: UserAndDokter)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}