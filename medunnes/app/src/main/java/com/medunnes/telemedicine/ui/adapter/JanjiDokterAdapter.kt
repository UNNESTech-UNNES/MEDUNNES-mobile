package com.medunnes.telemedicine.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.model.Messanger
import com.medunnes.telemedicine.databinding.MessageListBinding

class JanjiDokterAdapter(private val janjiList: ArrayList<Janji>) : RecyclerView.Adapter<JanjiDokterAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: MessageListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(janji: Janji) {
            with(binding) {
                tvPatientName.text = janji.pasienId.toString()
                tvPatientSession.text = "Sesi: " + janji.sesi
                tvMessangerStatus.text  = janji.dokterId.toString()
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
        fun onItemClicked(janji: Janji)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}