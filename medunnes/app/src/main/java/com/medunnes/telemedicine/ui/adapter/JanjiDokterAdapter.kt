package com.medunnes.telemedicine.ui.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.model.JanjiDanPasien
import com.medunnes.telemedicine.data.model.Messanger
import com.medunnes.telemedicine.databinding.MessageListBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale

class JanjiDokterAdapter(private val janjiList: ArrayList<JanjiDanPasien>) : RecyclerView.Adapter<JanjiDokterAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: MessageListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(janjiDanPasien: JanjiDanPasien) {
            with(binding) {
                val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
                val fullDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                val date = dateFormat.parse(janjiDanPasien.janji.dateTime)
                val status = janjiDanPasien.janji.status

                if (status == "Telah disetujui") {
                    tvMessangerStatus.setTextColor(root.resources.getColor(R.color.blue))
                } else if (status == "Tidak disetujui") {
                    tvMessangerStatus.setTextColor(root.resources.getColor(R.color.red))
                } else {
                    tvMessangerStatus.setTextColor(root.resources.getColor(R.color.fifth_color))
                }

                tvPatientName.text = janjiDanPasien.user.fullname
                tvPatientSession.text = "${fullDateFormat.format(date)}/${janjiDanPasien.janji.sesi}"
                tvMessangerStatus.text  = janjiDanPasien.janji.status
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = MessageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d("SIZE", janjiList.size.toString())
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
        fun onItemClicked(janjiDanPasien: JanjiDanPasien)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}