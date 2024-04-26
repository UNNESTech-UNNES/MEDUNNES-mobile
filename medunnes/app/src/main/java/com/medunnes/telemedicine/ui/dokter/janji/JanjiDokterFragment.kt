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
import com.medunnes.telemedicine.ui.pasien.janjiPasien.BuatJanjiDokterFragment
import kotlinx.coroutines.launch

class JanjiDokterFragment : Fragment() {
    private var _binding: FragmentJanjiDokterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LayananDokterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var messangersAdapter: JanjiDokterAdapter
    private val listJanji = ArrayList<Janji>()
    private val listJanjiAndPasien = ArrayList<JanjiDanPasien>()
    private val pdd = PasienDetailDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJanjiDokterBinding.inflate(inflater, container, false)
        lifecycleScope.launch { getJanjiByDokterId() }

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
                    putString(PasienDetailDialog.SESI_PASIEN, janjiDanPasien.janji.sesi)
                    putString(PasienDetailDialog.TANGGAL_PASIEN, janjiDanPasien.janji.dateTime)
                    putString(PasienDetailDialog.TELEPON_PASIEN, janjiDanPasien.user.noTelepon)
                }

                pdd.arguments = bundle
                pdd.show(childFragmentManager, PasienDetailDialog.TAG)

                pdd.setOnItemClickCallback(object : PasienDetailDialog.OnItemClickCallback {
                    override fun onItemClicked(isDisetujui: Boolean) {
                        if (isDisetujui) {
                            viewModel.updateJanjiPasien(Janji(
                                janjiDanPasien.janji.janjidId,
                                janjiDanPasien.janji.dateTime,
                                janjiDanPasien.janji.sesi,
                                "Telah disetujui",
                                janjiDanPasien.janji.dokterId,
                                janjiDanPasien.janji.pasienId
                            ))
                            pdd.dismiss()
                            restartFragment()
                        } else {
                            viewModel.updateJanjiPasien(Janji(
                                janjiDanPasien.janji.janjidId,
                                janjiDanPasien.janji.dateTime,
                                janjiDanPasien.janji.sesi,
                                "Tidak disetujui",
                                janjiDanPasien.janji.dokterId,
                                janjiDanPasien.janji.pasienId
                            ))
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
            .addToBackStack(null)
            .commit()
    }

//    private fun getAllJanji() {
//        viewModel.getJanji().observe(viewLifecycleOwner) { data ->
//            listJanji.addAll(data)
//            showRecycleList(listJanji)
//        }
//    }

    private suspend fun getJanjiByDokterId() {
        val uid = viewModel.getUserLogin()
        viewModel.getUserAndDokterId(uid).observe(viewLifecycleOwner) { data ->
            data.forEach {
                val dokterId = it.dokter.dokterId
                viewModel.getJanjiAndPasien(dokterId).observe(viewLifecycleOwner) { data ->
                    if (!data.isNullOrEmpty()) {
                        listJanjiAndPasien.addAll(data)
                        showRecycleList(listJanjiAndPasien)
                    } else {
                        Log.d("DATA", "Data is empty")
                    }
                }
            }
        }
    }

//    private fun searchMessanger() {
//        with(binding) {
//            searchView.setupWithSearchBar(searchBar)
//            searchView
//                .editText
//                .setOnEditorActionListener { _, _, _ ->
//                    searchBar.setText(searchView.text)
//                    searchView.hide()
//                    filteredListMessanger.clear()
//                    filteredListMessanger.addAll(getFilteredMessangerList("${searchView.text}"))
//                    showRecycleList()
//                    Log.d("GET", "${getFilteredMessangerList("${searchView.text}")}")
//                    false
//                }
//        }
//    }
}