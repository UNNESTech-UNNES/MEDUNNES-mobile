package com.medunnes.telemedicine.ui.dokter.janji

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.response.JanjiDataItem
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
                    if (janji.pasien.namaPasien == janji.pasienTambahan.namaPasienTambahan)
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
                                viewModel.updateJanji(
                                    janji.idJanji,
                                    janji.pasienId.toLong(),
                                    janji.dokterId.toLong(),
                                    janji.pasienTambahanId.toLong(),
                                    janji.sesiId.toLong(),
                                    janji.datetime,
                                    janji.catatan,
                                    if (isDisetujui) "accepted" else "rejected"
                                )

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

    private fun restartFragment() {
        val janjiDokterFragment = JanjiDokterFragment()
        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.dokter_frame_container, janjiDokterFragment, JanjiDokterFragment::class.java.simpleName)
            .commit()
    }

    private suspend fun getJanjiByDokterId(filter: String) {
        val listJanjiPasien = ArrayList<JanjiDataItem>()
        val uid = viewModel.getUserLogin()
        var dokterId: Int
        viewModel.getDokterByUserId(uid)
        viewModel.dokter.observe(viewLifecycleOwner) { data ->
            if (!data.isNullOrEmpty()) {
                dokterId = data[0].idDokter.toInt()
                viewModel.getJanjiByDokterId(dokterId)
                viewModel.janji.observe(viewLifecycleOwner) { janji ->
                    listJanjiPasien.clear()
                    listJanjiPasien.addAll(janji)
                    val filteredData = listJanjiPasien.filter { name ->
                        name.pasienTambahan.namaPasienTambahan.lowercase().contains(filter)
                    } as ArrayList<JanjiDataItem>
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
}