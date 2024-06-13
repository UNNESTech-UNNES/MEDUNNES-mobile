package com.medunnes.telemedicine.ui.pasien.janjiPasien

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
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.databinding.FragmentBuatJanjiBinding
import com.medunnes.telemedicine.ui.adapter.DokterListAdapter
import com.medunnes.telemedicine.ui.dialog.SpesialisBottomSheetDialog
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import kotlinx.coroutines.launch

class JanjiPasienFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentBuatJanjiBinding? = null
    private val binding get() = _binding!!
    private val listDokter = ArrayList<DokterDataItem>()
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBuatJanjiBinding.inflate(inflater, container, false)

        getDoctorList("")
        searchMessanger()

        binding.tvSpesialisasiAll.setOnClickListener {
            showBottomSheet()
        }

        with(binding) {
            btnDokterUmum.setOnClickListener(this@JanjiPasienFragment)
            btnKandungan.setOnClickListener(this@JanjiPasienFragment)
            btnPsikiater.setOnClickListener(this@JanjiPasienFragment)
            btnKulitDanKelamin.setOnClickListener(this@JanjiPasienFragment)
        }


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

    private fun getDoctorList(filter: String) {
        viewModel.getAllDokter(1)
        viewModel.dokter.observe(viewLifecycleOwner) { data ->
            if (!data.isNullOrEmpty()) {
                listDokter.clear()
                listDokter.addAll(data)
                val filteredData = listDokter.filter {
                    it.namaDokter.lowercase().contains(filter)
                } as ArrayList<DokterDataItem>
                showRecyclerList(filteredData)
            }
            Log.d("DOKTERS", data.toString())
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
                    getDoctorList("${searchView.text}")
                    false
                }
        }
    }

    private fun showBottomSheet() {
        val bsd = SpesialisBottomSheetDialog()
        childFragmentManager.let { bsd.show(it, SpesialisBottomSheetDialog.TAG) }

        bsd.setOnItemClickCallback(object : SpesialisBottomSheetDialog.OnItemClickCallback {
            override fun onItemClicked(speciality: String) {
                doctorBySpecialityFragment(speciality)
                bsd.dismiss()
            }

        })
    }

    private fun doctorBySpecialityFragment(speciality: String) {
        val doctorBySpecilityFragment = DoctorBySpecialityFragment()
        val fragmentManager = parentFragmentManager
        val bundle = Bundle()
        bundle.putString(DoctorBySpecialityFragment.SPECIALITY, speciality)
        doctorBySpecilityFragment.arguments = bundle

        fragmentManager.beginTransaction()
            .replace(R.id.pasien_frame_container, doctorBySpecilityFragment, DoctorBySpecialityFragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()
    }

    private fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                btnDokterUmum -> doctorBySpecialityFragment("Dokter Umum")
                btnKandungan -> doctorBySpecialityFragment("Kandungan")
                btnPsikiater -> doctorBySpecialityFragment("Psikiater")
                btnKulitDanKelamin -> doctorBySpecialityFragment("Kulit dan Kelamin")
            }
        }
    }
}