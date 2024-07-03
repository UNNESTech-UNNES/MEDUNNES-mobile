package com.medunnes.telemedicine.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.data.model.Artikel
import com.medunnes.telemedicine.data.model.Faskes
import com.medunnes.telemedicine.databinding.ListFaskesBinding

class FaskesAdapter(private val faskesList: ArrayList<Faskes>) : RecyclerView.Adapter<FaskesAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: ListFaskesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(faskes: Faskes) {
            with(binding) {
                tvFaskesNama.text = faskes.namaFaskes
                Glide.with(itemView.context)
                    .load(faskes.fotoFaskes)
                    .into(ivFaskesFoto)
                    .clearOnDetach()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListFaskesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(faskesList[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(faskesList[position])
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(faskes: Faskes)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}