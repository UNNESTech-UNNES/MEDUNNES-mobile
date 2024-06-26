package com.medunnes.telemedicine.ui.pasien.konsultasiPasien

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
import com.medunnes.telemedicine.data.response.KonsultasiDataItem
import com.medunnes.telemedicine.databinding.FragmentKonsultasiPasienBinding
import com.medunnes.telemedicine.ui.adapter.DokterKonsultasiAdapter
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import kotlinx.coroutines.launch

class KonsultasiPasienFragment : Fragment() {
    private var _binding: FragmentKonsultasiPasienBinding? = null
    private val binding get() = _binding!!
    private val listDokter = ArrayList<KonsultasiDataItem>()
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentKonsultasiPasienBinding.inflate(inflater, container, false)

        lifecycleScope.launch { getDoctorList("") }
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
                    val konsultasiDetailFragment = KonsultasiDetailFragment()
                    val fragment = parentFragmentManager
                    val bundle = Bundle()
                    bundle.putInt(KonsultasiDetailFragment.DOKTER_ID, konsultasi.dokterId)
                    bundle.putInt(KonsultasiDetailFragment.KONSULTASI_ID, konsultasi.idKonsultasi)
                    konsultasiDetailFragment.arguments = bundle
                    fragment.beginTransaction()
                        .replace(R.id.pasien_frame_container, konsultasiDetailFragment, KonsultasiDetailFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }
            })
        } else {
            binding.rvDoctorList.visibility = View.GONE
            binding.tvDataEmpty.visibility = View.VISIBLE
        }
    }

    private suspend fun getDoctorList(filter: String) {
        val uid = viewModel.getUserLoginId()
        Log.d("UID", uid.toString())
        viewModel.getPasienByUserLogin(uid)
        viewModel.pasien.observe(viewLifecycleOwner) { pasien ->
            val pasienId = pasien[0].idPasien
            viewModel.getKonsultasiByPasienId(pasienId.toInt())
            viewModel.konsultasi.observe(viewLifecycleOwner) { konsultasi ->
                listDokter.clear()
                listDokter.addAll(konsultasi)
                showRecyclerList(listDokter)
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

    private fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}