package com.medunnes.telemedicine.ui.adapter

import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.response.JanjiDataItem
import com.medunnes.telemedicine.databinding.MessageListBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class JanjiDokterAdapter(
    private val janjiList: ArrayList<JanjiDataItem>
) : RecyclerView.Adapter<JanjiDokterAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: MessageListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(janji: JanjiDataItem) {
            with(binding) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val fullDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

                val date = dateFormat.parse(janji.datetime)
                val status = janji.status
                tvPatientName.text = janji.pasienId.toString()
                if (status == "accepted") {
                    tvMessangerStatus.setTextColor(root.resources.getColor(R.color.blue))
                } else if (status == "rejected") {
                    tvMessangerStatus.setTextColor(root.resources.getColor(R.color.red))
                } else {
                    tvMessangerStatus.setTextColor(root.resources.getColor(R.color.fifth_color))
                }
                tvPatientSession.text = "${date?.let { fullDateFormat.format(it) }}/Sesi ${janji.sesiId}"
                tvMessangerStatus.text  = janji.status

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
        fun onItemClicked(janji: JanjiDataItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}