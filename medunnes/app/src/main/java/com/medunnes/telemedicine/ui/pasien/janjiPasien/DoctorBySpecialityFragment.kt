package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.databinding.FragmentDoctorBySpecialityBinding
import com.medunnes.telemedicine.ui.adapter.DokterListAdapter
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel

class DoctorBySpecialityFragment : Fragment() {
    private var _binding: FragmentDoctorBySpecialityBinding? = null
    private val binding get() = _binding!!
    private val listDokter = ArrayList<DokterDataItem>()
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var specialityId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDoctorBySpecialityBinding.inflate(inflater, container, false)

        val spesialisasi = resources.getStringArray(R.array.spesialissasi)

        specialityId = arguments?.getInt(SPECIALITY)!!
        binding.tvSpesialisasiTitle.text = getString(R.string.spesialis_dokter, spesialisasi[specialityId])
        getDoctorBySpeciality("", specialityId.toLong())
        searchMessanger()

        return binding.root
    }

    private fun showRecyclerList(listAdapter: ArrayList<DokterDataItem>) {
        binding.rvDoctorList.layoutManager = LinearLayoutManager(context)
        val dokterAdapter = DokterListAdapter(listAdapter)
        binding.rvDoctorList.adapter = dokterAdapter

        dokterAdapter.setOnItemClickCallback(object : DokterListAdapter.OnItemClickCallback {
            override fun onItemClicked(dokter: DokterDataItem) {
                val buatJanjiDokterFragment = BuatJanjiDokterFragment()
                val fragmentManager = parentFragmentManager
                val bundle = Bundle()
                bundle.putInt(BuatJanjiDokterFragment.DOCTOR_ID, dokter.idDokter.toInt())
                buatJanjiDokterFragment.arguments = bundle

                fragmentManager.beginTransaction()
                    .replace(R.id.pasien_frame_container, buatJanjiDokterFragment, BuatJanjiDokterFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }

        })
    }

    private fun getDoctorBySpeciality(filter: String, speciality: Long) {
        showProgressBar(true)
        viewModel.getAllDokter(1)
        viewModel.dokter.observe(viewLifecycleOwner) { data ->
            if (!data.isNullOrEmpty()) {
                showProgressBar(false)
                listDokter.clear()
                listDokter.addAll(data)

                val filteredData = listDokter.filter {
                    it.namaDokter.lowercase().contains(filter) &&
                            it.spesialisId == speciality+1 &&
                            it.status.contains("approve")
                } as ArrayList<DokterDataItem>
                showRecyclerList(filteredData)
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
                    getDoctorBySpeciality("${searchView.text}", specialityId.toLong())
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

    companion object {
        const val SPECIALITY = "speciality"
    }

}