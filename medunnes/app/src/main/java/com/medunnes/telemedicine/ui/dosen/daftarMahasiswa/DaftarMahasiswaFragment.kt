package com.medunnes.telemedicine.ui.dosen.daftarMahasiswa

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.databinding.FragmentDaftarMahasiswaBinding
import com.medunnes.telemedicine.ui.adapter.DokterListAdapter
import com.medunnes.telemedicine.ui.dosen.LayananDosenViewModel
import com.medunnes.telemedicine.ui.message.MessageActivity
import kotlinx.coroutines.launch

class DaftarMahasiswaFragment : Fragment() {
    private var _binding: FragmentDaftarMahasiswaBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LayananDosenViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val listDokter = ArrayList<DokterDataItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDaftarMahasiswaBinding.inflate(inflater, container, false)
        getDoctorList("")
        searchMessanger()
        return binding.root
    }

    private fun showRecyclerList(listAdapter: ArrayList<DokterDataItem>) {
        if (listAdapter.isNotEmpty()) {
            binding.tvDataEmpty.visibility = View.GONE
            binding.rvDoctorList.visibility = View.VISIBLE
            binding.rvDoctorList.layoutManager = LinearLayoutManager(context)
            val dokterAdapter = DokterListAdapter(listAdapter)
            binding.rvDoctorList.adapter = dokterAdapter

            dokterAdapter.setOnItemClickCallback(object : DokterListAdapter.OnItemClickCallback {
                override fun onItemClicked(dokter: DokterDataItem) {
                    // DO NOTHING
                }
            })
        } else {
            binding.rvDoctorList.visibility = View.GONE
            binding.tvDataEmpty.visibility = View.VISIBLE
        }
    }

    private fun getDoctorList(filter: String) {
        lifecycleScope.launch {
            showProgressBar(true)
            binding.tvDataEmpty.visibility = View.GONE
            val uid = viewModel.getUserLoginId()
            viewModel.getDosenById(uid)
            viewModel.dosen.observe(viewLifecycleOwner) { dosen ->
                val dosenId = dosen[0].idDosen
                viewModel.getDokterByDosen(dosenId)
                viewModel.mahasiswa.observe(viewLifecycleOwner) { mahasiswa ->
                    showProgressBar(false)
                    listDokter.clear()
                    listDokter.addAll(mahasiswa)

                    val filteredDokterList = listDokter.filter {
                        it.user.name.lowercase().contains(filter)
                    } as ArrayList<DokterDataItem>

                    showRecyclerList(filteredDokterList)
                }
            }
        }
    }

    private fun searchMessanger() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    lifecycleScope.launch { getDoctorList("${searchView.text}") }
                    false
                }
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}