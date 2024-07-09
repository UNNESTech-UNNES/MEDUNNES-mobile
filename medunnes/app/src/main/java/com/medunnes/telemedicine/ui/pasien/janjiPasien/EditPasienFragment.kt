package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.FragmentEditPasienBinding
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditPasienFragment : Fragment(),
    View.OnClickListener,
    AdapterView.OnItemSelectedListener

{
    private lateinit var binding: FragmentEditPasienBinding
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var spinnerKelamin: String = ""
    private var datePicked: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditPasienBinding.inflate(inflater, container, false)

        setPasien()
        setSpinner()

        binding.tilPasienTglLahir.setEndIconOnClickListener { showDatePicker() }
        binding.btnSimpan.setOnClickListener(this)

        return binding.root
    }

    private fun setPasien() {
        val pasienId = arguments?.getInt(PASIEN_ID)
        val pasienTambahanId = arguments?.getInt(PASIEN_TAMBAHAN_ID)
        viewModel.getPasienTambahanById(pasienId!!, pasienTambahanId!!)
        viewModel.pasienTambahan.observe(viewLifecycleOwner) { data ->
            if (!data.isNullOrEmpty()) {
                data.forEach {
                    with(binding) {
                        tiePasienNama.setText(it.namaPasienTambahan)
                        tiePasienHubungan.setText(it.hubunganKeluarga)
                        tiePasienTglLahir.setText(it.tglLahir)
                        tiePasienTb.setText(it.tB)
                        tiePasienBb.setText(it.bB)
                        tiePasienTambahanId.setText(it.pasienId.toString())

                        if (it.jenisKelamin == "L") {
                            spinnerPasienJenisKelamin.setSelection(0)
                        } else {
                            spinnerPasienJenisKelamin.setSelection(1)
                        }
                    }
                }
            }
        }
    }

    private fun updatePasien() {
        val pasienTambahanId = arguments?.getInt(PASIEN_TAMBAHAN_ID)
        try {
            lifecycleScope.launch {
                viewModel.updatePasienTambahan(
                    pasienTambahanId!!,
                    binding.tiePasienTambahanId.text.toString().toLong(),
                    "${binding.tiePasienNama.text}",
                    binding.tiePasienTb.text.toString().toInt(),
                    binding.tiePasienBb.text.toString().toInt(),
                    spinnerKelamin,
                    "${binding.tiePasienTglLahir.text}",
                    "${binding.tiePasienHubungan.text}"
                )
            }

            Toast.makeText(context, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.d("UPDATE PASIEN FAIL", e.message.toString())
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val cYear = calendar.get(Calendar.YEAR)
        val cMonth = calendar.get(Calendar.MONTH)
        val cDay = calendar.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, month, day ->
                calendar.set(year, month, day)
                val fullDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
                val date = fullDateFormat.format(calendar.time)
                binding.tiePasienTglLahir.setText(date)
                datePicked = date

            }, cYear, cMonth, cDay)

        datePickerDialog.show()
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
        spinnerKelamin = if (pos == 0) "L" else "P"
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // DO NOTHING
    }

    override fun onClick(view: View) {
        when(view) {
            binding.btnSimpan -> updatePasien()
        }
    }

    companion object {
        const val PASIEN_ID = "pasien_id"
        const val PASIEN_TAMBAHAN_ID = "pasien_tambahan_id"
    }

}