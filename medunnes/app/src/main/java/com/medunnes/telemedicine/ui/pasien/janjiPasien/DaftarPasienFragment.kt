package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.os.Bundle
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
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.databinding.FragmentDaftarPasienBinding
import com.medunnes.telemedicine.ui.adapter.PasienListAdapter
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import kotlinx.coroutines.launch

class DaftarPasienFragment : Fragment() {
    private var _binding: FragmentDaftarPasienBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val listPasien = ArrayList<Pasien>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDaftarPasienBinding.inflate(inflater, container, false)

        lifecycleScope.launch { getDataPasien() }

        binding.btnTambahPasien.setOnClickListener {
            val tambahPasienFragment = TambahPasienFragment()
            val parentManager = parentFragmentManager

            parentManager.beginTransaction()
                .replace(R.id.pasien_frame_container, tambahPasienFragment, TambahPasienFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    private suspend fun getDataPasien() {
        val uid = viewModel.getUserLoginId()
        viewModel.getPasienByUser(uid).observe(viewLifecycleOwner) { data ->
            listPasien.clear()
            listPasien.addAll(data)
            showRecyclerList(listPasien)
        }
    }

    private fun showRecyclerList(listAdapter: ArrayList<Pasien>) {
        binding.rvPatientList.layoutManager = LinearLayoutManager(context)
        val pasienAdapter = PasienListAdapter(listAdapter)
        binding.rvPatientList.setItemViewCacheSize(listAdapter.size)
        binding.rvPatientList.adapter = pasienAdapter

        pasienAdapter.setOnItemClickCallback(object : PasienListAdapter.OnItemClickCallback {
            override fun onItemClicked(pasien: Pasien) {

            }

            override fun onEditButtonClicked(pasien: Pasien) {
                Toast.makeText(context, "Edit Button", Toast.LENGTH_SHORT).show()
            }

            override fun onRadioButtonChecked(pasien: Pasien) {
                Toast.makeText(context, pasien.namaPasien, Toast.LENGTH_SHORT).show()
            }

        })
    }

}