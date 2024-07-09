package com.medunnes.telemedicine.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.data.model.Artikel
import com.medunnes.telemedicine.databinding.ListArticlesBinding

class ArticlesAdapter(private val articleList: ArrayList<Artikel>) : RecyclerView.Adapter<ArticlesAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: ListArticlesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artikel: Artikel) {
            with(binding) {
                tvArticleTitle.text = artikel.judulArtikel
                Glide.with(itemView.context)
                    .load(artikel.headlineArtikel)
                    .into(ivHeadline)
                    .clearOnDetach()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(articleList[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(articleList[position])
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(artikel: Artikel)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}