package com.medunnes.telemedicine.ui.dokter.janji

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.model.JanjiDanPasien
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
    private val listJanjiAndPasien = ArrayList<JanjiDanPasien>()
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

    private fun showRecycleList(listAdapter: ArrayList<JanjiDanPasien>) {
        binding.rvMessageList.layoutManager = LinearLayoutManager(context)
        messangersAdapter = JanjiDokterAdapter(listAdapter)
        binding.rvMessageList.adapter = messangersAdapter

        messangersAdapter.setOnItemClickCallback(object : JanjiDokterAdapter.OnItemClickCallback {
            override fun onItemClicked(janjiDanPasien: JanjiDanPasien) {
                val bundle = Bundle()
                with(bundle) {
                    putString(PasienDetailDialog.NAMA_PASIEN, janjiDanPasien.user.fullname)
                    putString(PasienDetailDialog.TELEPON_PASIEN, janjiDanPasien.user.noTelepon)
                    putString(PasienDetailDialog.SESI_PASIEN, janjiDanPasien.janji.sesi)
                    putString(PasienDetailDialog.TANGGAL_PASIEN, janjiDanPasien.janji.dateTime)
                }

                pdd.arguments = bundle
                pdd.show(childFragmentManager, PasienDetailDialog.TAG)

                pdd.setOnItemClickCallback(object : PasienDetailDialog.OnItemClickCallback {
                    override fun onItemClicked(isDisetujui: Boolean) {
                        if (isDisetujui) {
                            with(janjiDanPasien) {
                                viewModel.updateJanjiPasien(Janji(
                                    janji.janjidId,
                                    janji.dateTime,
                                    janji.sesi,
                                    "Telah disetujui",
                                    janji.dokterId,
                                    janji.pasienId
                                ))
                            }
                            pdd.dismiss()
                            restartFragment()
                        } else {
                            with(janjiDanPasien) {
                                viewModel.updateJanjiPasien(Janji(
                                    janji.janjidId,
                                    janji.dateTime,
                                    janji.sesi,
                                    "Tidak disetujui",
                                    janji.dokterId,
                                    janji.pasienId
                                ))
                            }
                            pdd.dismiss()
                            restartFragment()
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
        val uid = viewModel.getUserLogin()
        viewModel.getUserAndDokterId(uid).observe(viewLifecycleOwner) { data ->
            data.forEach {
                val dokterId = it.dokter.dokterId
                viewModel.getJanjiAndPasien(dokterId).observe(viewLifecycleOwner) { data ->
                    Log.d("PASIEN", data.toString())
                    if (!data.isNullOrEmpty()) {
                        listJanjiAndPasien.clear()
                        listJanjiAndPasien.addAll(data)
                        val filteredData = listJanjiAndPasien.filter { name ->
                            name.user.fullname.lowercase().contains(filter)
                        } as ArrayList<JanjiDanPasien>
                        showRecycleList(filteredData)
                    } else {
                        Log.d("DATA", "Data is empty")
                    }
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