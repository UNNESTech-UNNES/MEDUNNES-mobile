package com.medunnes.telemedicine.ui.dosen.konsultasi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.response.KonsultasiDataItem
import com.medunnes.telemedicine.databinding.FragmentKonsultasiBinding
import com.medunnes.telemedicine.databinding.FragmentKonsultasiDosenBinding
import com.medunnes.telemedicine.ui.adapter.DokterKonsultasiAdapter
import com.medunnes.telemedicine.ui.dosen.LayananDosenViewModel
import com.medunnes.telemedicine.ui.message.MessageActivity
import com.medunnes.telemedicine.ui.pasien.konsultasiPasien.KonsultasiDetailFragment
import kotlinx.coroutines.launch

class KonsultasiDosenFragment : Fragment() {
    private var _binding: FragmentKonsultasiDosenBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LayananDosenViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val listDokter = ArrayList<KonsultasiDataItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentKonsultasiDosenBinding.inflate(inflater, container, false)
        getDoctorList("")
        searchMessanger()
        return binding.root
    }

    private fun showRecyclerList(listAdapter: ArrayList<KonsultasiDataItem>) {
        if (listAdapter.isNotEmpty()) {
            binding.tvDataEmpty.visibility = View.GONE
            binding.rvDoctorList.visibility = View.VISIBLE
            binding.rvDoctorList.layoutManager = LinearLayoutManager(context)
            val dokterAdapter = DokterKonsultasiAdapter(listAdapter)
            binding.rvDoctorList.adapter = dokterAdapter

            dokterAdapter.setOnItemClickCallback(object : DokterKonsultasiAdapter.OnItemClickCallback {
                override fun onItemClicked(konsultasi: KonsultasiDataItem) {
                    val intent = Intent(context, MessageActivity::class.java)
                    intent.putExtra(MessageActivity.KONSULTASI_ID, konsultasi.idKonsultasi.toInt())
                    intent.putExtra(MessageActivity.DOKTER_ID, konsultasi.dokterId.toInt())
                    intent.putExtra(MessageActivity.PASIEN_ID, konsultasi.pasienId.toInt())
                    startActivity(intent)
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
                    mahasiswa.forEach { mhs ->
                        val dokterId = mhs.idDokter
                        viewModel.getKonsultasiByDokter(dokterId.toInt())
                        viewModel.konsultasi.observe(viewLifecycleOwner) { konsultasi ->
                            if (!konsultasi.isNullOrEmpty()) {
                                binding.tvDataEmpty.visibility = View.GONE
                                showProgressBar(false)
                                listDokter.addAll(konsultasi)
                                val filteredDokterList = konsultasi.filter {
                                    it.dokter.status.contains("approve") &&
                                            it.status.contains("berlangsung")
                                } as ArrayList<KonsultasiDataItem>

                                if (filteredDokterList.isNullOrEmpty()) {
                                    showProgressBar(false)
                                    binding.tvDataEmpty.visibility = View.VISIBLE
                                }
                                showRecyclerList(filteredDokterList)
                            } else {
                                showProgressBar(false)
                                binding.tvDataEmpty.visibility = View.VISIBLE
                            }
                        }
                    }
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