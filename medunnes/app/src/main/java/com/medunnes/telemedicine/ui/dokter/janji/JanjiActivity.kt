package com.medunnes.telemedicine.ui.dokter.janji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.model.Messanger
import com.medunnes.telemedicine.databinding.ActivityJanjiBinding
import com.medunnes.telemedicine.ui.adapter.JanjiDokterAdapter

class JanjiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJanjiBinding
    private val listMessanger = ArrayList<Messanger>()
    private val filteredListMessanger = ArrayList<Messanger>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJanjiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRecycleList(getMessangerList())
        searchMessanger()
    }

    private fun showRecycleList(listAdapter: ArrayList<Messanger>) {
        binding.rvMessageList.layoutManager = LinearLayoutManager(this@JanjiActivity)
        val messangersAdapter = JanjiDokterAdapter(listAdapter)
        binding.rvMessageList.adapter = messangersAdapter

        messangersAdapter.setOnItemClickCallback(object : JanjiDokterAdapter.OnItemClickCallback {
            override fun onItemClicked(messanger: Messanger) {
                Toast.makeText(this@JanjiActivity, "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getMessangerList() : ArrayList<Messanger> {
        val fotoMessanger = resources.getStringArray(R.array.poster_film)
        val namaMessanger = resources.getStringArray(R.array.film_judul)
        val sesiMessanger = resources.getStringArray(R.array.rilis_tahun)
        val statusMessanger = resources.getStringArray(R.array.rilis_tahun)
        listMessanger.clear()
        if (namaMessanger.isNotEmpty()) {
            for (i in namaMessanger.indices) {
                val messanger = Messanger(fotoMessanger[i], namaMessanger[i], sesiMessanger[i], statusMessanger[i])
                listMessanger.add(messanger)
            }
        }
        return listMessanger
    }

    private fun getFilteredMessangerList(filter: String): ArrayList<Messanger> {
        return getMessangerList().filter {
            it.namaMessanger.lowercase().contains(filter.lowercase())
        } as ArrayList<Messanger>
    }

    private fun searchMessanger() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    filteredListMessanger.clear()
                    filteredListMessanger.addAll(getFilteredMessangerList("${searchView.text}"))
                    showRecycleList(filteredListMessanger)
                    Log.d("GET", "${getFilteredMessangerList("${searchView.text}")}")
                    false
                }
        }
    }
}