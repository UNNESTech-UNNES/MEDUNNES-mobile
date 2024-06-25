package com.medunnes.telemedicine.ui.dokter.konsultasi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.Konsultasi
import com.medunnes.telemedicine.data.response.KonsultasiDataItem
import com.medunnes.telemedicine.databinding.FragmentKonsultasiBinding
import com.medunnes.telemedicine.ui.adapter.KonsultasiAdapter
import com.medunnes.telemedicine.ui.dokter.LayananDokterViewModel
import com.medunnes.telemedicine.ui.message.MessageViewModel
import kotlinx.coroutines.launch

class KonsultasiFragment : Fragment() {

    private var _binding: FragmentKonsultasiBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LayananDokterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val listPatient = ArrayList<KonsultasiDataItem>()
    private val filteredListPatient = ArrayList<KonsultasiDataItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentKonsultasiBinding.inflate(inflater, container, false)
        searchPatient()
        lifecycleScope.launch { getPatientList() }

        return binding.root
    }

    private fun showRecycleList(listAdapter: ArrayList<KonsultasiDataItem>) {
        binding.rvKonsultasiList.layoutManager = LinearLayoutManager(context)
        val adapter = KonsultasiAdapter(listAdapter)
        binding.rvKonsultasiList.adapter = adapter

        adapter.setOnItemClickCallback(object : KonsultasiAdapter.OnItemClickCallback {
            override fun onItemClicked(konsultasi: KonsultasiDataItem) {
                Toast.makeText(context, "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private suspend fun getPatientList() {
        val uid = viewModel.getUserLogin()
        viewModel.getDokterByUserId(uid)
        viewModel.dokter.observe(viewLifecycleOwner) { dokter ->
            val dokterId = dokter[0].idDokter.toInt()
            viewModel.getKonsultasiByDokter(dokterId)
            viewModel.konsultasi.observe(viewLifecycleOwner) { konsultasi ->
                listPatient.clear()
                listPatient.addAll(konsultasi)

                showRecycleList(listPatient)
            }
        }
    }

//    private fun getFilteredMessangerList(filter: String): ArrayList<Konsultasi> {
//        return getPatientList().filter {
//            it.namaPatient.lowercase().contains(filter.lowercase())
//        } as ArrayList<Konsultasi>
//    }

    private fun searchPatient() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    filteredListPatient.clear()
                    //filteredListPatient.addAll(getFilteredMessangerList("${searchView.text}"))
                    showRecycleList(filteredListPatient)
                    false

                }
        }
    }

    companion object {
        const val DOKTER_ID = "dokter_id"
    }
}