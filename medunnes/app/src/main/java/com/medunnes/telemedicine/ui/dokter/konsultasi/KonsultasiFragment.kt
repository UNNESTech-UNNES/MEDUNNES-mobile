package com.medunnes.telemedicine.ui.dokter.konsultasi

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
import com.medunnes.telemedicine.data.response.KonsultasiDataItem
import com.medunnes.telemedicine.databinding.FragmentKonsultasiBinding
import com.medunnes.telemedicine.ui.adapter.KonsultasiAdapter
import com.medunnes.telemedicine.ui.dokter.LayananDokterViewModel
import com.medunnes.telemedicine.ui.message.MessageActivity
import com.medunnes.telemedicine.ui.pasien.konsultasiPasien.KonsultasiDetailFragment
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
        lifecycleScope.launch { getPatientList("") }

        return binding.root
    }

    private fun showRecycleList(listAdapter: ArrayList<KonsultasiDataItem>) {
        binding.rvKonsultasiList.layoutManager = LinearLayoutManager(context)
        val adapter = KonsultasiAdapter(listAdapter)
        binding.rvKonsultasiList.adapter = adapter

        adapter.setOnItemClickCallback(object : KonsultasiAdapter.OnItemClickCallback {
            override fun onItemClicked(konsultasi: KonsultasiDataItem) {
                intentMessage(konsultasi.pasienId.toInt(), konsultasi.idKonsultasi.toInt())
            }

        })
    }

    private suspend fun getPatientList(filter: String) {
        showProgressBar(true)
        val uid = viewModel.getUserLogin()
        viewModel.getDokterByUserId(uid)
        viewModel.dokter.observe(viewLifecycleOwner) { dokter ->
            val dokterId = dokter[0].idDokter.toInt()
            viewModel.getKonsultasiByDokter(dokterId)
            viewModel.konsultasi.observe(viewLifecycleOwner) { konsultasi ->
                if (!konsultasi.isNullOrEmpty()) {
                    showProgressBar(false)
                    listPatient.clear()
                    listPatient.addAll(konsultasi)
                    val filteredKonsultasiList = konsultasi.filter {
                        it.pasien.namaPasien.lowercase().contains(filter)
                    } as ArrayList<KonsultasiDataItem>

                    if (filteredKonsultasiList.isEmpty()) {
                        binding.tvDataEmpty.visibility = View.VISIBLE
                    }

                    showRecycleList(filteredKonsultasiList)
                } else {
                    showProgressBar(false)
                    binding.tvDataEmpty.visibility = View.VISIBLE
                }

            }
        }
    }

    private fun intentMessage(id: Int, konId: Int) {
        lifecycleScope.launch {
            val role = viewModel.getUserRole()
            if (role == 1) {
                val intent = Intent(context, MessageActivity::class.java)
                intent.putExtra(MessageActivity.PASIEN_ID, id)
                intent.putExtra(MessageActivity.KONSULTASI_ID, konId)
                startActivity(intent)
            } else {
                val dokterId = arguments?.getInt(KonsultasiDetailFragment.DOKTER_ID)
                if (dokterId != null) {
                    val intent = Intent(context, MessageActivity::class.java)
                    intent.putExtra(MessageActivity.DOKTER_ID, dokterId)
                    intent.putExtra(MessageActivity.KONSULTASI_ID, konId)
                    startActivity(intent)
                }

            }
        }
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
                    lifecycleScope.launch { getPatientList("${searchView.text}") }
                    showRecycleList(filteredListPatient)
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