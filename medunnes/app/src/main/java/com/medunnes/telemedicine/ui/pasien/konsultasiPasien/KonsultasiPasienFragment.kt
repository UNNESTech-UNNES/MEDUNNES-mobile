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
import com.medunnes.telemedicine.data.model.JanjiDanPasien
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.databinding.FragmentBuatJanjiBinding
import com.medunnes.telemedicine.databinding.FragmentKonsultasiPasienBinding
import com.medunnes.telemedicine.ui.adapter.DokterKonsultasiAdapter
import com.medunnes.telemedicine.ui.adapter.DokterListAdapter
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import kotlinx.coroutines.launch

class KonsultasiPasienFragment : Fragment() {
    private var _binding: FragmentKonsultasiPasienBinding? = null
    private val binding get() = _binding!!
    private val listDokter = ArrayList<UserAndDokter>()
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

    private fun showRecyclerList(listAdapter: ArrayList<UserAndDokter>) {
        if (listAdapter.isNotEmpty()) {
            binding.tvDataEmpty.visibility = View.GONE
            binding.rvDoctorList.visibility = View.VISIBLE
            binding.rvDoctorList.layoutManager = LinearLayoutManager(context)
            val dokterAdapter = DokterKonsultasiAdapter(listAdapter)
            binding.rvDoctorList.adapter = dokterAdapter

            dokterAdapter.setOnItemClickCallback(object : DokterKonsultasiAdapter.OnItemClickCallback {
                override fun onItemClicked(dokter: UserAndDokter) {
                    val konsultasiDetailFragment = KonsultasiDetailFragment()
                    val fragment = parentFragmentManager
                    val bundle = Bundle()
                    bundle.putInt(KonsultasiDetailFragment.DOKTER_ID, dokter.user.id)
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
        viewModel.getDokterByJanji(uid).observe(viewLifecycleOwner) { data ->
            listDokter.clear()
            data.forEach {
                val dokterId = it.janji.dokterId
                viewModel.getDokterByDokterId(dokterId).observe(viewLifecycleOwner) { data1 ->
                    if (!data.isNullOrEmpty()) {
                        listDokter.addAll(data1)
                        if (listDokter.isNotEmpty()) {
                            val filteredData = listDokter.filter {
                                it.user.fullname.lowercase().contains(filter) } as ArrayList<UserAndDokter>
                            showRecyclerList(filteredData)
                            Log.d("FILDATA", filteredData.toString())
                        } else {
                            binding.tvDataEmpty.visibility = View.VISIBLE
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

    private fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}