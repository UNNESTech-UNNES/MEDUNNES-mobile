package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.databinding.FragmentDoctorBySpecialityBinding
import com.medunnes.telemedicine.ui.adapter.DokterListAdapter
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel

class DoctorBySpecialityFragment : Fragment() {
    private var _binding: FragmentDoctorBySpecialityBinding? = null
    private val binding get() = _binding!!
    private val listDokter = ArrayList<UserAndDokter>()
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var speciality: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDoctorBySpecialityBinding.inflate(inflater, container, false)

        speciality = "${arguments?.getString(SPECIALITY)}"
        binding.tvSpesialisasiTitle.setText(getString(R.string.spesialis_dokter, speciality))

        //getDoctorBySpeciality("", 1)
        searchMessanger()

        return binding.root
    }

    private fun showRecyclerList(listAdapter: ArrayList<UserAndDokter>) {
        binding.rvDoctorList.layoutManager = LinearLayoutManager(context)
        val dokterAdapter = DokterListAdapter(listAdapter)
        binding.rvDoctorList.adapter = dokterAdapter

        dokterAdapter.setOnItemClickCallback(object : DokterListAdapter.OnItemClickCallback {
            override fun onItemClicked(dokter: UserAndDokter) {
                val buatJanjiDokterFragment = BuatJanjiDokterFragment()
                val fragmentManager = parentFragmentManager
                val bundle = Bundle()
                bundle.putInt(BuatJanjiDokterFragment.DOCTOR_ID, dokter.user.id)
                buatJanjiDokterFragment.arguments = bundle

                fragmentManager.beginTransaction()
                    .replace(R.id.pasien_frame_container, buatJanjiDokterFragment, BuatJanjiDokterFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }

        })
    }

    private fun getDoctorBySpeciality(filter: String, speciality: Int) {
        viewModel.getDokterBySpeciality(speciality).observe(viewLifecycleOwner) { data ->
            listDokter.clear()
            listDokter.addAll(data)

            val filteredData = listDokter.filter {
                it.user.fullname.lowercase().contains(filter)
            } as ArrayList<UserAndDokter>
            showRecyclerList(filteredData)
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
                    //getDoctorBySpeciality("${searchView.text}", 1)
                    false
                }
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val SPECIALITY = "speciality"
    }

}