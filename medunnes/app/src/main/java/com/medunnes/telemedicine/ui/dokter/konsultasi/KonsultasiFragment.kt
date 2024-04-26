package com.medunnes.telemedicine.ui.dokter.konsultasi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.model.Konsultasi
import com.medunnes.telemedicine.databinding.FragmentKonsultasiBinding
import com.medunnes.telemedicine.ui.adapter.KonsultasiAdapter

class KonsultasiFragment : Fragment() {

    private var _binding: FragmentKonsultasiBinding? = null
    private val binding get() = _binding!!

    private val listPatient = ArrayList<Konsultasi>()
    private val filteredListPatient = ArrayList<Konsultasi>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentKonsultasiBinding.inflate(inflater, container, false)

        showRecycleList(getPatientList())
        searchPatient()

        return binding.root
    }

    private fun showRecycleList(listAdapter: ArrayList<Konsultasi>) {
        binding.rvKonsultasiList.layoutManager = LinearLayoutManager(context)
        val messangersAdapter = KonsultasiAdapter(listAdapter)
        binding.rvKonsultasiList.adapter = messangersAdapter

        messangersAdapter.setOnItemClickCallback(object : KonsultasiAdapter.OnItemClickCallback {
            override fun onItemClicked(konsultasi: Konsultasi) {
                Toast.makeText(context, "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getPatientList() : ArrayList<Konsultasi> {
        val fotoMessanger = resources.getStringArray(R.array.poster_film)
        val namaMessanger = resources.getStringArray(R.array.film_judul)
        val sesiMessanger = resources.getStringArray(R.array.rilis_tahun)
        val statusMessanger = resources.getStringArray(R.array.rilis_tahun)
        listPatient.clear()
        if (namaMessanger.isNotEmpty()) {
            for (i in namaMessanger.indices) {
                val messanger = Konsultasi(fotoMessanger[i], namaMessanger[i], sesiMessanger[i], statusMessanger[i])
                listPatient.add(messanger)
            }
        }
        return listPatient
    }

    private fun getFilteredMessangerList(filter: String): ArrayList<Konsultasi> {
        return getPatientList().filter {
            it.namaPatient.lowercase().contains(filter.lowercase())
        } as ArrayList<Konsultasi>
    }

    private fun searchPatient() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    filteredListPatient.clear()
                    filteredListPatient.addAll(getFilteredMessangerList("${searchView.text}"))
                    showRecycleList(filteredListPatient)
                    Log.d("GET", "${getFilteredMessangerList("${searchView.text}")}")
                    false

                }
        }
    }

}