package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.response.DokterDataItem
import com.medunnes.telemedicine.databinding.FragmentBuatJanjiBinding
import com.medunnes.telemedicine.ui.adapter.DokterListAdapter
import com.medunnes.telemedicine.ui.dialog.SpesialisBottomSheetDialog
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel

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
        showProgressBar(true)
        viewModel.getAllDokter(1)
        viewModel.dokter.observe(viewLifecycleOwner) { data ->
            if (!data.isNullOrEmpty()) {
                binding.tvDataEmpty.visibility = View.GONE
                showProgressBar(false)
                listDokter.clear()
                listDokter.addAll(data)
                val filteredData = listDokter.filter {
                    it.status.contains("approve")
                } as ArrayList<DokterDataItem>

                if (filteredData.isNullOrEmpty()) {
                    showProgressBar(false)
                    binding.tvDataEmpty.visibility = View.VISIBLE
                }
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
                    getDoctorList("${searchView.text}")
                    false
                }
        }
    }

    private fun showBottomSheet() {
        val bsd = SpesialisBottomSheetDialog()
        childFragmentManager.let { bsd.show(it, SpesialisBottomSheetDialog.TAG) }

        bsd.setOnItemClickCallback(object : SpesialisBottomSheetDialog.OnItemClickCallback {
            override fun onItemClicked(speciality: Int) {
                doctorBySpecialityFragment(speciality)
                bsd.dismiss()
            }

        })
    }

    private fun doctorBySpecialityFragment(speciality: Int) {
        val doctorBySpecilityFragment = DoctorBySpecialityFragment()
        val fragmentManager = parentFragmentManager
        val bundle = Bundle()
        bundle.putInt(DoctorBySpecialityFragment.SPECIALITY, speciality)
        doctorBySpecilityFragment.arguments = bundle

        fragmentManager.beginTransaction()
            .replace(R.id.pasien_frame_container, doctorBySpecilityFragment, DoctorBySpecialityFragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()
    }

    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                btnDokterUmum -> doctorBySpecialityFragment(0)
                btnKandungan -> doctorBySpecialityFragment(1)
                btnPsikiater -> doctorBySpecialityFragment(2)
                btnKulitDanKelamin -> doctorBySpecialityFragment(3)
            }
        }
    }
}