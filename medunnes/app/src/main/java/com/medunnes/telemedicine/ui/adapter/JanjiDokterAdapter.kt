package com.medunnes.telemedicine.ui.adapter

import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.model.JanjiDanPasien
import com.medunnes.telemedicine.databinding.MessageListBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class JanjiDokterAdapter(private val janjiList: ArrayList<JanjiDanPasien>) : RecyclerView.Adapter<JanjiDokterAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: MessageListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(janjiDanPasien: JanjiDanPasien) {
            with(binding) {
                val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
                val fullDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

                with(janjiDanPasien) {
                    val date = dateFormat.parse(janji.dateTime)
                    val status = janji.status
                    tvPatientName.text = janjiDanPasien.user.fullname
                    if (status == "Telah disetujui") {
                        tvMessangerStatus.setTextColor(root.resources.getColor(R.color.blue))
                    } else if (status == "Tidak disetujui") {
                        tvMessangerStatus.setTextColor(root.resources.getColor(R.color.red))
                    } else {
                        tvMessangerStatus.setTextColor(root.resources.getColor(R.color.fifth_color))
                    }
                    tvPatientSession.text = "${date?.let { fullDateFormat.format(it) }}/${janji.sesi}"
                    tvMessangerStatus.text  = janji.status
                }

                val path = Environment.getExternalStorageDirectory()
                val imageFile = "${File(path, "/Android/data/com.medunnes.telemedicine${janjiDanPasien.user.image}")}"
                Glide.with(itemView.context)
                    .load(imageFile)
                    .into(ivMessanger)
                    .clearOnDetach()

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = MessageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        fun onItemClicked(janjiDanPasien: JanjiDanPasien)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}