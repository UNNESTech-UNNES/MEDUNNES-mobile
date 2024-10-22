package com.medunnes.telemedicine.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medunnes.telemedicine.data.response.PasienTambahanDataItem
import com.medunnes.telemedicine.databinding.ListPasienBinding

class PasienListAdapter(private val listPasien: ArrayList<PasienTambahanDataItem>) : RecyclerView.Adapter<PasienListAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: ListPasienBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pasien: PasienTambahanDataItem) {
            with(binding) {
                tvPasienNama.text = pasien.namaPasienTambahan
                tvPasienHubungan.text = pasien.hubunganKeluarga
            }
        }
        val radioButton = binding.rbtnPasien
        val editButton = binding.ivEditPasien
        val deleteButton = binding.ivDeletePasien
        val tvPasienHubungan = binding.tvPasienHubungan
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListPasienBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listPasien.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        holder.bind(listPasien[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listPasien[position])
        }

        if (holder.tvPasienHubungan.text == "Diri sendiri") {
            holder.editButton.visibility = View.GONE
            holder.deleteButton.visibility = View.GONE
        }

        holder.radioButton.isChecked = selectedItem == position
        holder.radioButton.setOnClickListener {
            if (selectedItem != position) {
                notifyItemChanged(selectedItem)
                selectedItem = holder.adapterPosition
                notifyItemChanged(selectedItem)
                onItemClickCallback.onRadioButtonChecked(listPasien[position])
            }
        }

        holder.editButton.setOnClickListener {
            onItemClickCallback.onEditButtonClicked(listPasien[position])
        }

        holder.deleteButton.setOnClickListener {
            onItemClickCallback.onDeleteButtonClicked(listPasien[position])
        }
    }

    private var selectedItem: Int = 0
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(pasien: PasienTambahanDataItem)
        fun onEditButtonClicked(pasien: PasienTambahanDataItem)
        fun onDeleteButtonClicked(pasien: PasienTambahanDataItem)
        fun onRadioButtonChecked(pasien: PasienTambahanDataItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}