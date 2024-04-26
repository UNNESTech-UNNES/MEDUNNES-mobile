package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.Janji
import com.medunnes.telemedicine.data.model.Sesi
import com.medunnes.telemedicine.databinding.FragmentBuatJanjiDokterBinding
import com.medunnes.telemedicine.ui.adapter.SesiAdapter
import com.medunnes.telemedicine.ui.dialog.BuatJanjiConfirmationDialog
import com.medunnes.telemedicine.ui.dialog.BuatJanjiSuccessDialog
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BuatJanjiDokterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentBuatJanjiDokterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var datePicked: String
    private var listSesi = ArrayList<Sesi>()

    private val bjcd = BuatJanjiConfirmationDialog()
    private val bjsd = BuatJanjiSuccessDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBuatJanjiDokterBinding.inflate(inflater, container, false)
        val doctorId = arguments?.getInt(DOCTOR_ID)
        Log.d("ID", "$doctorId")
        setDoctorProfile()

        lifecycleScope.launch { setPasienData() }

        with(binding) {
            tilJanjiTanggal.setEndIconOnClickListener { showDatePicker() }
            btnBuatJanjiDokter.setOnClickListener(this@BuatJanjiDokterFragment)
            tvFormIsEmpty.visibility = View.VISIBLE
            tvSesiPicked.visibility = View.GONE
            tvDatePicked.visibility = View.GONE
        }

        Log.d("SESI", "${getSesiList()}")
        showRecycleList()

        return binding.root
    }

    private fun setDoctorProfile() {
        val doctorId = arguments?.getInt(DOCTOR_ID)
        doctorId?.let {
            viewModel.getDokterById(it).observe(viewLifecycleOwner) { data ->
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
    }

    suspend fun setPasienData() {
        viewModel.getUserProfile(viewModel.getUserLoginId()).observe(viewLifecycleOwner) { data ->
            data.forEach {
                binding.tiePasienPicked.setText(it.fullname)
                binding.tiePasienIdPicked.setText("${it.id}")
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

                binding.tvFormIsEmpty.visibility = View.GONE
                binding.tvDatePicked.visibility = View.VISIBLE

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
                with(binding) {
                    tvFormIsEmpty.visibility = View.GONE
                    tvSesiPicked.visibility = View.VISIBLE
                    tvSesiPicked.text = getString(R.string.no_sesi, sesi.noSesi)
                }
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

    private fun postJanji(isConfirm: Boolean) {
        val doctorId = arguments?.getInt(DOCTOR_ID)
        if (isConfirm) {
            bjcd.dismiss()
            try {
                doctorId?.let {
                    viewModel.getDokterById(it).observe(viewLifecycleOwner) { data ->
                        data.forEach {
                            viewModel.insertJanjiPasien(Janji(
                                0,
                                datePicked,
                                "${binding.tvSesiPicked.text}",
                                "Belum disetujui",
                                it.dokter.dokterId,
                                "${binding.tiePasienIdPicked.text}".toInt()
                                ))
                        }
                    }
                }
                showSuccessDialog()
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        } else {
            bjcd.dismiss()
        }
    }

    private fun showConfirmationDialog() {
        bjcd.show(childFragmentManager, BuatJanjiConfirmationDialog.TAG)

        bjcd.setOnItemClickCallback(object : BuatJanjiConfirmationDialog.OnItemClickCallback {
            override fun onItemClicked(isConfirm: Boolean) {
                postJanji(isConfirm)
            }

        })
    }

    private fun showSuccessDialog() {
        bjsd.show(childFragmentManager, BuatJanjiSuccessDialog.TAG)
    }

    companion object {
        const val DOCTOR_ID = "doctor_id"
    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                tilJanjiTanggal -> showDatePicker()
                btnBuatJanjiDokter -> showConfirmationDialog()
            }
        }
    }
}