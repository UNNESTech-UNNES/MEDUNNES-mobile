package com.medunnes.telemedicine.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medunnes.telemedicine.data.model.Sesi
import com.medunnes.telemedicine.databinding.SesiDokterListBinding

class SesiAdapter(private val sesiLis: ArrayList<Sesi>): RecyclerView.Adapter<SesiAdapter.ListViewModel>() {
    class ListViewModel(private val binding: SesiDokterListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(sesi: Sesi) {
            with(binding) {
                tvSesi.text = "Sesi ${sesi.noSesi}"
                tvSesiPukul.text = sesi.waktuSesi
            }
        }

        val tableRow = binding.tblRowSesiTop
        val cardView = binding.cvSesiDokter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewModel {
        val binding = SesiDokterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewModel(binding)
    }

    override fun getItemCount(): Int = sesiLis.size

    override fun onBindViewHolder(holder: ListViewModel, position: Int) {
        holder.bind(sesiLis[position])

        //var isClicked = true
        if (selectedItem == position) {
            holder.tableRow.setBackgroundColor(Color.parseColor("#E6C38C"))
            holder.cardView.elevation = 10F
        } else {
            holder.tableRow.setBackgroundColor(Color.parseColor("#E1C8A5"))
            holder.cardView.elevation = 0F
        }

        holder.itemView.setOnClickListener {
            if (selectedItem != position) {
                notifyItemChanged(selectedItem)
                selectedItem = holder.adapterPosition
                notifyItemChanged(selectedItem)
                onItemClickCallback.onClick(sesiLis[position])
            }
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    private var selectedItem: Int = -1

    interface OnItemClickCallback {
        fun onClick(sesi: Sesi)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}