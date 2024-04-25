package com.medunnes.telemedicine.ui.dokter.janji

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
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.model.Messanger
import com.medunnes.telemedicine.databinding.FragmentJanjiDokterBinding
import com.medunnes.telemedicine.ui.adapter.JanjiDokterAdapter
import com.medunnes.telemedicine.ui.dokter.LayananDokterViewModel
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel

class JanjiDokterFragment : Fragment() {
    private var _binding: FragmentJanjiDokterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LayananDokterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val listJanji = ArrayList<Janji>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJanjiDokterBinding.inflate(inflater, container, false)
        showRecycleList()

        return binding.root
    }

    private fun showRecycleList() {
        getAllJanji()
        binding.rvMessageList.layoutManager = LinearLayoutManager(context)
        val messangersAdapter = JanjiDokterAdapter(listJanji)
        binding.rvMessageList.adapter = messangersAdapter

        messangersAdapter.setOnItemClickCallback(object : JanjiDokterAdapter.OnItemClickCallback {
            override fun onItemClicked(janji: Janji) {
                Toast.makeText(context, "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getAllJanji() {
        viewModel.getJanji().observe(viewLifecycleOwner) { data ->
            listJanji.addAll(data)
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