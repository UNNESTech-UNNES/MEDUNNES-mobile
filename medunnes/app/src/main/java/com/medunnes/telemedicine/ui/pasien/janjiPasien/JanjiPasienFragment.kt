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
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.databinding.FragmentBuatJanjiBinding
import com.medunnes.telemedicine.ui.adapter.DokterListAdapter
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel

class JanjiPasienFragment : Fragment() {
    private var _binding: FragmentBuatJanjiBinding? = null
    private val binding get() = _binding!!
    private val listDokter = ArrayList<UserAndDokter>()
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

        return binding.root
    }

    private fun showRecyclerList(listAdapter: ArrayList<UserAndDokter>) {
        binding.rvDoctorList.layoutManager = LinearLayoutManager(context)
        val dokterAdapter = DokterListAdapter(listAdapter)
        binding.rvDoctorList.adapter = dokterAdapter

        dokterAdapter.setOnItemClickCallback(object : DokterListAdapter.OnItemClickCallback {
            override fun onItemClicked(dokter: UserAndDokter) {
                makeToast("Fitur belum tersedia")
            }

        })
    }

    private fun getDoctorList(filter: String) {
        viewModel.getAllDokter().observe(viewLifecycleOwner) { data ->
            listDokter.clear()
            listDokter.addAll(data)
            val filteredData = listDokter.filter {
                it.user.fullname.lowercase().contains(filter) } as ArrayList<UserAndDokter>
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
                    getDoctorList("${searchView.text}")
                    Log.d("GET", "${getDoctorList("${searchView.text}")}")
                    false
                }
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}