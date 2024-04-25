package com.medunnes.telemedicine.ui.adapter

import android.R.string
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.model.Sesi
import com.medunnes.telemedicine.databinding.SesiDokterListBinding
import kotlin.coroutines.coroutineContext

class SesiAdapter(private val sesiLis: ArrayList<Sesi>): RecyclerView.Adapter<SesiAdapter.ListViewModel>() {
    private var sesi1 =  ArrayList<Int>()
    class ListViewModel(private val binding: SesiDokterListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(sesi: Sesi) {
            with(binding) {
                tvSesi.text = "Sesi ${sesi.noSesi}"
                tvSesiPukul.text = sesi.waktuSesi
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewModel {
        val binding = SesiDokterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewModel(binding)
    }

    override fun getItemCount(): Int = sesiLis.size

    override fun onBindViewHolder(holder: ListViewModel, position: Int) {
        holder.bind(sesiLis[position])

        //var isClicked = true
        holder.itemView.setOnClickListener {
//            if (isClicked) {
//                sesi1.add(position)
//                it.elevation = 100.0F
//                isClicked = false
//                Log.d("ARRSESI", sesi1.toString())
//                if (position  == sesi1.last()) {
//                    it.setBackgroundColor(it.resources.getColor(R.color.grey_less))
//                } else {
//                    it.setBackgroundColor(it.resources.getColor(R.color.white))
//                }
//                if (sesi == sesiLis[position]) {
//                    it.elevation = 100.0F
//                    //it.setBackgroundColor(it.resources.getColor(R.color.grey_less))
//                    isClicked = false
//                    sesi1.add(position)
//                } else {
//                    it.elevation = 0.0F
//                    //it.setBackgroundColor(it.resources.getColor(R.color.grey_less))
//                    isClicked = true
//                }
//            } else {
//                it.elevation = 0.0F
//                //it.setBackgroundColor(it.resources.getColor(R.color.white))
//                isClicked = true
//                Log.d("CL", "${sesiLis[position]}")
//            }
            onItemClickCallback.onClick(sesiLis[position])
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onClick(sesi: Sesi)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}