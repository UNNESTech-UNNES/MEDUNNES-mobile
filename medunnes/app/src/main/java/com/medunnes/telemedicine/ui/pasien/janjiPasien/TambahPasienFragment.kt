package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.FragmentTambahPasienBinding
import com.medunnes.telemedicine.ui.dialog.BuatJanjiConfirmationDialog
import com.medunnes.telemedicine.ui.dialog.BuatJanjiSuccessDialog
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TambahPasienFragment : Fragment(),
    View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentTambahPasienBinding
    private lateinit var dataSpinner: String
    private lateinit var datePicked: String
    private val bjcd = BuatJanjiConfirmationDialog()
    private val bjsd = BuatJanjiSuccessDialog()
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTambahPasienBinding.inflate(layoutInflater)
        setSpinner()

        binding.tilPasienTglLahir.setEndIconOnClickListener { showDatePicker() }

        binding.btnSimpan.setOnClickListener(this)


        return binding.root
    }

    private fun setSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.jenis_kelamin,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPasienJenisKelamin.adapter = arrayAdapter
        }
        binding.spinnerPasienJenisKelamin.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        dataSpinner = if (pos == 0) "L" else "P"
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // DO NOTHING
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val cYear = calendar.get(Calendar.YEAR)
        val cMonth = calendar.get(Calendar.MONTH)
        val cDay = calendar.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, month, day ->
                calendar.set(year, month, day)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = dateFormat.format(calendar.time)
                binding.tiePasienTglLahir.setText(date)
                datePicked = date
            }, cYear, cMonth, cDay)

        datePickerDialog.show()
    }

    private suspend fun insertPasien(isConfirm: Boolean) {
        if (isConfirm) {
            try {
                val uid = viewModel.getUserLoginId()
                viewModel.getPasienByUserLogin(uid)
                viewModel.pasien.observe(viewLifecycleOwner) { data ->
                    lifecycleScope.launch {
                        viewModel.insertPasienTambahan(
                            data[0].idPasien,
                            "${binding.tiePasienNama.text}",
                            binding.tiePasienTb.text.toString().toInt(),
                            binding.tiePasienBb.text.toString().toInt(),
                            datePicked,
                            "${binding.tiePasienHubungan.text}"
                        )
                    }
                }
                bjcd.dismiss()
                showSuccessDialog()
            } catch (e: Exception) {
                Log.d("INSERT DATA FAIL", e.message.toString())
            }
        } else {
            bjcd.dismiss()
        }
    }

    private fun showConfirmationDialog() {
        val bundle = Bundle()
        bundle.putString(BuatJanjiConfirmationDialog.DIALOG_TITLE, "Tambahkan pasien?")
        bundle.putString(BuatJanjiConfirmationDialog.DIALOG, "Pastikan data sudah benar")
        bjcd.arguments = bundle
        bjcd.show(childFragmentManager, BuatJanjiConfirmationDialog.TAG)

        bjcd.setOnItemClickCallback(object : BuatJanjiConfirmationDialog.OnItemClickCallback {
            override fun onItemClicked(isConfirm: Boolean) {
                lifecycleScope.launch { insertPasien(isConfirm) }
            }
        })
    }

    private fun showSuccessDialog() {
        val bundle = Bundle()
        bundle.putString(BuatJanjiConfirmationDialog.DIALOG, "Pasien berhasil ditambahkan")
        bjcd.arguments = bundle
        bjsd.show(childFragmentManager, BuatJanjiSuccessDialog.TAG)
    }

    override fun onClick(view: View) {
        when(view) {
            binding.btnSimpan -> showConfirmationDialog()
        }
    }
}