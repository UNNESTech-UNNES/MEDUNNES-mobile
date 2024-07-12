package com.medunnes.telemedicine.ui.dokter.janji

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
import com.medunnes.telemedicine.data.response.JanjiDataItem
import com.medunnes.telemedicine.data.response.KonsultasiResponse
import com.medunnes.telemedicine.databinding.FragmentJanjiDokterBinding
import com.medunnes.telemedicine.ui.adapter.JanjiDokterAdapter
import com.medunnes.telemedicine.ui.dialog.PasienDetailDialog
import com.medunnes.telemedicine.ui.dokter.LayananDokterViewModel
import kotlinx.coroutines.launch

class JanjiDokterFragment : Fragment() {
    private var _binding: FragmentJanjiDokterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LayananDokterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var messangersAdapter: JanjiDokterAdapter
    private val pdd = PasienDetailDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentJanjiDokterBinding.inflate(inflater, container, false)
        lifecycleScope.launch { getJanjiByDokterId("") }
        searchPasien()

        return binding.root
    }

    private fun showRecycleList(
        listAdapter: ArrayList<JanjiDataItem>,
    ) {
        binding.rvMessageList.layoutManager = LinearLayoutManager(context)
        messangersAdapter = JanjiDokterAdapter(listAdapter)
        binding.rvMessageList.adapter = messangersAdapter

        messangersAdapter.setOnItemClickCallback(object : JanjiDokterAdapter.OnItemClickCallback {
            override fun onItemClicked(janji: JanjiDataItem) {
                val bundle = Bundle()
                with(bundle) {
                    putString(PasienDetailDialog.IMG_PASIEN, janji.pasien.imgPasien)
                    putString(PasienDetailDialog.NAMA_PASIEN, janji.pasienTambahan.namaPasienTambahan)
                    putString(PasienDetailDialog.SESI_PASIEN, "${janji.sesiId}")
                    putString(PasienDetailDialog.TANGGAL_PASIEN, janji.datetime)
                    putString(PasienDetailDialog.CATATAN, janji.catatan)
                }

                pdd.arguments = bundle
                pdd.show(childFragmentManager, PasienDetailDialog.TAG)

                pdd.setOnItemClickCallback(object : PasienDetailDialog.OnItemClickCallback {
                    override fun onItemClicked(isDisetujui: Boolean) {
                        lifecycleScope.launch {
                            try {
                                val pasienId = janji.pasienId.toLong()
                                val dokterId = janji.dokterId.toLong()
                                val topik = janji.catatan
                                viewModel.updateJanji(
                                    janji.idJanji,
                                    pasienId,
                                    dokterId,
                                    janji.pasienTambahanId.toLong(),
                                    janji.sesiId.toLong(),
                                    janji.datetime,
                                    topik,
                                    if (isDisetujui) "accepted" else "rejected"
                                )

                                if (isDisetujui) {
                                    val insKonsultasi = insertKonsultasi(
                                        pasienId,
                                        dokterId,
                                        topik,
                                        "berlangsung"
                                    )
                                    val konsultasiId = insKonsultasi.data[0].idKonsultasi
                                    insertDiskusi(konsultasiId.toLong(), "memulai diskusi")
                                }
                                restartFragment()
                            } catch (e: Exception) {
                                Log.d("UPDATE JANJI FAIL", e.message.toString())
                            }
                        }
                    }

                })
            }

        })
    }

    private suspend fun insertKonsultasi(
        pasienId: Long,
        dokterId: Long,
        topik: String,
        status: String
    ): KonsultasiResponse = viewModel.insertKonsultasi(pasienId, dokterId, topik, status)

    private suspend fun insertDiskusi(
        konsultasiId: Long,
        message: String
    ) {
        viewModel.insertDiskusi(konsultasiId, message)
    }

    private fun restartFragment() {
        val janjiDokterFragment = JanjiDokterFragment()
        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.dokter_frame_container, janjiDokterFragment, JanjiDokterFragment::class.java.simpleName)
            .commit()
    }

    private suspend fun getJanjiByDokterId(filter: String) {
        showProgressBar(true)
        val listJanjiPasien = ArrayList<JanjiDataItem>()
        val uid = viewModel.getUserLogin()
        var dokterId: Int
        viewModel.getDokterByUserId(uid)
        viewModel.dokter.observe(viewLifecycleOwner) { data ->
            if (!data.isNullOrEmpty()) {
                dokterId = data[0].idDokter.toInt()
                viewModel.getJanjiByDokterId(dokterId)
                viewModel.janji.observe(viewLifecycleOwner) { janji ->
                    showProgressBar(false)
                    binding.tvDataEmpty.visibility = View.GONE
                    listJanjiPasien.clear()
                    listJanjiPasien.addAll(janji)
                    val filteredData = listJanjiPasien.filter { name ->
                        name.pasienTambahan.namaPasienTambahan.lowercase().contains(filter)
                    } as ArrayList<JanjiDataItem>

                    if (filteredData.isNullOrEmpty()) {
                        binding.tvDataEmpty.visibility = View.VISIBLE
                    }
                    showRecycleList(filteredData)
                }
            }
        }
    }

    private fun searchPasien() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    lifecycleScope.launch { getJanjiByDokterId("${searchView.text}") }
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