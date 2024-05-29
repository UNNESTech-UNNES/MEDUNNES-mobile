package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.databinding.FragmentEditPasienBinding
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
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
    private var spinnerHubungan: String = ""
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
        if (pasienId != null) {
            viewModel.getPasienById(pasienId).observe(viewLifecycleOwner) { data ->
                data.forEach {
                    with(binding) {
                        datePicked = "${it.tanggalLahir}"
                        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
                        val fullDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                        val date = dateFormat.parse(datePicked)
                        tiePasienNama.setText(it.namaPasien)
                        tiePasienTglLahir.setText(date?.let { it1 -> fullDateFormat.format(it1) })

                        if (it.hubungan == "Keluarga") {
                            spinnerPasienHubungan.setSelection(0)
                        } else if (it.hubungan == "Teman") {
                            spinnerPasienHubungan.setSelection(1)
                        } else {
                            spinnerPasienHubungan.setSelection(2)
                        }
                    }
                }
            }
        }
    }

    private fun updatePasien() {
        val pasienId = arguments?.getInt(PASIEN_ID)
        try {
            if (pasienId != null) {
                viewModel.getPasienById(pasienId).observe(viewLifecycleOwner) { data ->
                    data.forEach {
                        viewModel.updatePasien(
                            Pasien(
                                it.pasienId,
                                "${binding.tiePasienNama.text}",
                                spinnerHubungan,
                                datePicked,
                                it.kartuIdentitas,
                                it.userId
                            )
                        )
                    }
                }
            }

            Toast.makeText(context, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.d("ERROR", e.toString())
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
                val fullDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                binding.tiePasienTglLahir.setText(fullDateFormat.format(calendar.time))
                datePicked = "${calendar.time}"

            }, cYear, cMonth, cDay)

        datePickerDialog.show()
    }

    private fun setSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.hubungan_pasien,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPasienHubungan.adapter = arrayAdapter
        }
        binding.spinnerPasienHubungan.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        spinnerHubungan = "${parent?.getItemAtPosition(pos)}"
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
    }

}