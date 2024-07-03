package com.medunnes.telemedicine.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.response.JanjiDataItem
import com.medunnes.telemedicine.databinding.ListKonsultasiDokterBinding
import com.medunnes.telemedicine.utils.imageBaseUrl
import java.text.SimpleDateFormat
import java.util.Locale

class JanjiDokterAdapter(
    private val janjiList: ArrayList<JanjiDataItem>
) : RecyclerView.Adapter<JanjiDokterAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: ListKonsultasiDokterBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        @Suppress("DEPRECATION")
        fun bind(janji: JanjiDataItem) {
            with(binding) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val fullDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                val date = dateFormat.parse(janji.datetime)
                val status = janji.status
                tvPatientName.text = janji.pasienTambahan.namaPasienTambahan
                when (status) {
                    "accepted" -> {
                        tvMessangerStatus.setTextColor(root.resources.getColor(R.color.blue))
                    }
                    "rejected" -> {
                        tvMessangerStatus.setTextColor(root.resources.getColor(R.color.red))
                    }
                    else -> {
                        tvMessangerStatus.setTextColor(root.resources.getColor(R.color.fifth_color))
                    }
                }
                tvPatientSession.text = "${date?.let { fullDateFormat.format(it) }}/Sesi ${janji.sesiId}"
                tvMessangerStatus.text  = janji.status

                if (janji.pasien.namaPasien == janji.pasienTambahan.namaPasienTambahan) {
                    val imagePath = "${imageBaseUrl()}/${janji.pasien.imgPasien}"
                    Glide.with(itemView.context)
                        .load(imagePath)
                        .into(ivMessanger)
                        .clearOnDetach()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListKonsultasiDokterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return janjiList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(janjiList[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(janjiList[position])
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(janji: JanjiDataItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}