package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.databinding.FragmentTambahPasienBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TambahPasienFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentTambahPasienBinding
    private lateinit var dataSpinner: String
    private lateinit var datePicked: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTambahPasienBinding.inflate(layoutInflater)
        setSpinner()

        binding.tilPasienTglLahir.setEndIconOnClickListener { showDatePicker() }

        return binding.root
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
        dataSpinner = "${parent?.getItemAtPosition(pos)}"
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
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.tiePasienTglLahir.setText(dateFormat.format(calendar.time))
                datePicked = "${calendar.time}"
            }, cYear, cMonth, cDay)

        datePickerDialog.show()
    }
}