package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.Artikel
import com.medunnes.telemedicine.data.model.Sesi
import com.medunnes.telemedicine.databinding.FragmentBuatJanjiDokterBinding
import com.medunnes.telemedicine.ui.adapter.ArticlesAdapter
import com.medunnes.telemedicine.ui.adapter.SesiAdapter
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BuatJanjiDokterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentBuatJanjiDokterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var datePicked: String = "date"
    private var listSesi = ArrayList<Sesi>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBuatJanjiDokterBinding.inflate(inflater, container, false)
        val doctorId = arguments?.getInt(DOCTOR_ID)
        Log.d("ID", "$doctorId")
        if (doctorId != null) setDoctorProfile(doctorId)

        lifecycleScope.launch { setPasienData() }

        with(binding) {
            tilJanjiTanggal.setEndIconOnClickListener { showDatePicker() }
            btnBuatJanjiDokter.setOnClickListener(this@BuatJanjiDokterFragment)
        }

        Log.d("SESI", "${getSesiList()}")
        showRecycleList()

        return binding.root
    }

    private fun setDoctorProfile(doctorId: Int) {
        viewModel.getDokterById(doctorId).observe(viewLifecycleOwner) { data ->
            data.forEach {
                with(binding) {
                    tvDoctorName.text = getString(R.string.nama_and_titel,
                        it.dokter.titelDepan, it.user.fullname, it.dokter.titelBelakang
                    )
                    tvDoctorExperience.text = it.dokter.pendidikan
                    tvDoctorSpeciality.text = it.dokter.spesialis
                }
            }

        }
    }

    suspend fun setPasienData() {
        viewModel.getUserProfile(viewModel.getUserLoginId()).observe(viewLifecycleOwner) { data ->
            data.forEach {
                binding.tiePasienPicked.setText(it.fullname)
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance(Locale("id", "ID"))
        val cYear = calendar.get(Calendar.YEAR)
        val cMonth = calendar.get(Calendar.MONTH)
        val cDay = calendar.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, month, day ->
                calendar.set(year, month, day)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fullDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                binding.tieSesiDate.setText(dateFormat.format(calendar.time))
                binding.tvDatePicked.setText(fullDateFormat.format(calendar.time))
                datePicked = "${calendar.time}"

            }, cYear, cMonth, cDay)

        datePickerDialog.show()
    }

    private fun showRecycleList() {
        getSesiList()
        val orientation = resources.configuration.orientation
        var column = 0
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            column = 2
        } else {
            column = 4
        }
        binding.rvSesiList.layoutManager = GridLayoutManager(context, column)
        val sesiAdapter = SesiAdapter(listSesi)
        binding.rvSesiList.adapter = sesiAdapter

        sesiAdapter.setOnItemClickCallback(object : SesiAdapter.OnItemClickCallback {
            override fun onClick(sesi: Sesi) {
                binding.tvSesiPicked.text = getString(R.string.no_sesi, sesi.noSesi)
            }

        })

    }

    private fun getSesiList() : ArrayList<Sesi> {
        val waktuSesi = resources.getStringArray(R.array.sesi_dokter)
        listSesi.clear()
        for (i in waktuSesi.indices) {
            val sesi = Sesi(i+1, waktuSesi[i])
            listSesi.add(sesi)
        }
        return listSesi
    }

    companion object {
        const val DOCTOR_ID = "doctor_id"
    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                tilJanjiTanggal -> showDatePicker()
                btnBuatJanjiDokter -> Toast.makeText(context, datePicked, Toast.LENGTH_SHORT).show()
            }
        }
    }
}