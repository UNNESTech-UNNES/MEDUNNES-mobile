package com.medunnes.telemedicine.ui.histories

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.response.KonsultasiDataItem
import com.medunnes.telemedicine.databinding.FragmentHistoriesBinding
import com.medunnes.telemedicine.ui.adapter.HistoriesAdapter
import com.medunnes.telemedicine.ui.adapter.HistoriesDokterAdapter
import com.medunnes.telemedicine.ui.message.MessageActivity
import kotlinx.coroutines.launch

class HistoriesFragment : Fragment() {

    private var _binding: FragmentHistoriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HistoriesViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val listKonsultasi = ArrayList<KonsultasiDataItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoriesBinding.inflate(inflater, container, false)
        setViewDifference("")
        searchMessanger()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setViewDifference(filter: String) {
        lifecycleScope.launch {
            viewModel.getUserLoginId()
            val role = viewModel.getUserRole()
            if (role == 1) {
                getPatientList(filter)
            } else {
                getDoctorList(filter)
            }
        }
    }

    private fun showDokterRecyclerList(listAdapter: ArrayList<KonsultasiDataItem>) {
        if (listAdapter.isNotEmpty()) {
            binding.tvDataEmpty.visibility = View.GONE
            binding.rvHistoriesList.visibility = View.VISIBLE
            binding.rvHistoriesList.layoutManager = LinearLayoutManager(context)
            val dokterAdapter = HistoriesAdapter(listAdapter)
            binding.rvHistoriesList.adapter = dokterAdapter

            dokterAdapter.setOnItemClickCallback(object : HistoriesAdapter.OnItemClickCallback {
                override fun onItemClicked(konsultasi: KonsultasiDataItem) {
                    val intent = Intent(context, MessageActivity::class.java)
                    intent.putExtra(MessageActivity.KONSULTASI_ID, konsultasi.idKonsultasi.toInt())
                    intent.putExtra(MessageActivity.PASIEN_ID, konsultasi.pasienId.toInt())
                    intent.putExtra(MessageActivity.DOKTER_ID, konsultasi.dokterId.toInt())
                    startActivity(intent)
                }
            })
        } else {
            binding.rvHistoriesList.visibility = View.GONE
            binding.tvDataEmpty.visibility = View.VISIBLE
        }
    }

    private fun showPatientRecyclerList(listAdapter: ArrayList<KonsultasiDataItem>) {
        if (listAdapter.isNotEmpty()) {
            binding.tvDataEmpty.visibility = View.GONE
            binding.rvHistoriesList.visibility = View.VISIBLE
            binding.rvHistoriesList.layoutManager = LinearLayoutManager(context)
            val dokterAdapter = HistoriesDokterAdapter(listAdapter)
            binding.rvHistoriesList.adapter = dokterAdapter

            dokterAdapter.setOnItemClickCallback(object : HistoriesDokterAdapter.OnItemClickCallback {
                override fun onItemClicked(konsultasi: KonsultasiDataItem) {
                    val intent = Intent(context, MessageActivity::class.java)
                    intent.putExtra(MessageActivity.KONSULTASI_ID, konsultasi.idKonsultasi.toInt())
                    intent.putExtra(MessageActivity.PASIEN_ID, konsultasi.pasienId.toInt())
                    intent.putExtra(MessageActivity.DOKTER_ID, konsultasi.dokterId.toInt())
                    startActivity(intent)
                }
            })
        } else {
            binding.rvHistoriesList.visibility = View.GONE
            binding.tvDataEmpty.visibility = View.VISIBLE
        }
    }

    private fun getDoctorList(filter: String) {
        showProgressBar(true)
        lifecycleScope.launch {
            binding.tvDataEmpty.visibility = View.GONE
            val uid = viewModel.getUserLoginId()
            viewModel.getPasienByUserLogin(uid)
            viewModel.pasien.observe(viewLifecycleOwner) { pasien ->
                val pasienId = pasien[0].idPasien
                viewModel.getKonsultasiByPasienId(pasienId.toInt())
                viewModel.konsultasi.observe(viewLifecycleOwner) { konsultasi ->
                    if (!konsultasi.isNullOrEmpty()) {
                        binding.tvDataEmpty.visibility = View.GONE
                        showProgressBar(false)
                        listKonsultasi.clear()
                        listKonsultasi.addAll(konsultasi)
                        val filteredDokterList = konsultasi.filter {
                            it.dokter.user.name.lowercase().contains(filter) &&
                            it.status.contains("berakhir")
                        } as ArrayList<KonsultasiDataItem>

                        if (filteredDokterList.isEmpty()) {
                            showProgressBar(false)
                            binding.tvDataEmpty.visibility = View.VISIBLE
                        }
                        showDokterRecyclerList(filteredDokterList)
                    } else {
                        showProgressBar(false)
                        binding.tvDataEmpty.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private suspend fun getPatientList(filter: String) {
        showProgressBar(true)
        val uid = viewModel.getUserLoginId()
        viewModel.getDokterByUserId(uid)
        viewModel.dokter.observe(viewLifecycleOwner) { dokter ->
            val dokterId = dokter[0].idDokter.toInt()
            viewModel.getKonsultasiByDokterId(dokterId)
            viewModel.konsultasi.observe(viewLifecycleOwner) { konsultasi ->
                if (!konsultasi.isNullOrEmpty()) {
                    showProgressBar(false)
                    listKonsultasi.clear()
                    listKonsultasi.addAll(konsultasi)
                    val filteredKonsultasiList = konsultasi.filter {
                        it.pasien.user.name.lowercase().contains(filter) &&
                        it.status.contains("berakhir")
                    } as ArrayList<KonsultasiDataItem>

                    if (filteredKonsultasiList.isEmpty()) {
                        binding.tvDataEmpty.visibility = View.VISIBLE
                    }

                    showPatientRecyclerList(filteredKonsultasiList)
                } else {
                    showProgressBar(false)
                    binding.tvDataEmpty.visibility = View.VISIBLE
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
                    setViewDifference("${searchView.text}")
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
}