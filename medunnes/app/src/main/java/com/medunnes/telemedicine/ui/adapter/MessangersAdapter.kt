package com.medunnes.telemedicine.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.data.model.Artikel
import com.medunnes.telemedicine.data.model.Messanger
import com.medunnes.telemedicine.databinding.MessageListBinding

class MessangersAdapter(private val messangerList: ArrayList<Messanger>) : RecyclerView.Adapter<MessangersAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: MessageListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(messanger: Messanger) {
            with(binding) {
                tvMessangerName.text = messanger.namaMessanger
                tvMessangerSession.text = "Sesi: " + messanger.sesiMessanger
                tvMessangerStatus.text  = messanger.startusMessanger
                Glide.with(itemView.context)
                    .load(messanger.fotoMessanger)
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
        Log.d("SIZE", messangerList.size.toString())
        return messangerList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(messangerList[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(messangerList[position])
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(messanger: Messanger)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}