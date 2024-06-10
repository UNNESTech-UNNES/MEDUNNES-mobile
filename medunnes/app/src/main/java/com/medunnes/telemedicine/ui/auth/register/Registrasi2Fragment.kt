package com.medunnes.telemedicine.ui.auth.register

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.databinding.FragmentRegistrasi2Binding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Registrasi2Fragment :
    Fragment(),
    View.OnClickListener,
    AdapterView.OnItemSelectedListener {

    private var _binding: FragmentRegistrasi2Binding? = null
    private val binding get() = _binding!!
    private lateinit var dataSpinner: String
    private var datePicked = "date"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrasi2Binding.inflate(inflater, container, false)
        val root: View = binding.root

        setSpinner()
        binding.btnLanjut1.setOnClickListener(this)

        binding.tilTglLulus.setEndIconOnClickListener {
            showDatePicker()
        }

        return root
    }

    private fun setSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.jenis_kelamin,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerKelamin.adapter = arrayAdapter
        }
        binding.spinnerKelamin.onItemSelectedListener = this
    }

    private fun bundle() : Bundle {
        val bundle = Bundle()
        with(bundle) {
            putString(Registrasi3Fragment.EMAIL, getDataBundle(EMAIL))
            putString(Registrasi3Fragment.FULLNAME, getDataBundle(FULLNAME))
            putString(Registrasi3Fragment.TITLE_ONE, getDataBundle(TITLE_ONE))
            putString(Registrasi3Fragment.TITLE_TWO, getDataBundle(TITLE_TWO))
            putLong(Registrasi3Fragment.NO_STR, arguments?.getLong(NO_STR)!!)
            putInt(Registrasi3Fragment.ROLE, arguments?.getInt(ROLE)!!)
            putString(Registrasi3Fragment.GENDER, dataSpinner)

            with(binding) {
                putString(Registrasi3Fragment.DATE, datePicked)
                putString(Registrasi3Fragment.ADDRESS, "${tieAlamat.text}")
                putString(Registrasi3Fragment.PLACE, "${tieTempatPraktik.text}")
                putInt(Registrasi3Fragment.GRADUATE_YEAR, tieTahunLulus.text?.toString()?.toInt() ?: 0)
            }

        }

        return bundle
    }

    private fun inputValidation(): Boolean {
        with(binding) {
            return (!getDataBundle(EMAIL).isNullOrEmpty()
                    && !getDataBundle(FULLNAME).isNullOrEmpty()
                    && !getDataBundle(TITLE_ONE).isNullOrEmpty()
                    && !getDataBundle(TITLE_TWO).isNullOrEmpty()
                    && arguments?.getLong(NO_STR) !== null
                    && dataSpinner.isNotEmpty()
                    && datePicked.isNotEmpty()
                    && !tieAlamat.text.isNullOrEmpty()
                    && !tieTempatPraktik.text.isNullOrEmpty()
                    && !tieTahunLulus.text.isNullOrEmpty())
        }
    }

    private fun getDataBundle(data: String) = arguments?.getString(data)

    override fun onClick(view: View?) {
        when(view) {
            binding.btnLanjut1 -> {
                val registrasi3Fragment = Registrasi3Fragment()
                val fragmentManager = parentFragmentManager

                if (inputValidation()) {
                    registrasi3Fragment.arguments = bundle()
                    fragmentManager.beginTransaction().apply {
                        replace(R.id.frame_container, registrasi3Fragment, Registrasi3Fragment::class.java.simpleName)
                        addToBackStack(null)
                        commit()
                    }
                } else {
                    Toast.makeText(context, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        dataSpinner = if (pos == 1) "P" else "L"
        Toast.makeText(context, dataSpinner, Toast.LENGTH_SHORT).show()
        getDataSpinner(dataSpinner)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // DO NOTHING
    }

    private fun getDataSpinner(gender: String): String = gender

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val cYear = calendar.get(Calendar.YEAR)
        val cMonth = calendar.get(Calendar.MONTH)
        val cDay = calendar.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, month, day ->
                calendar.set(year, month, day)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateFormatted =  dateFormat.format(calendar.time)
                binding.tieTglLulus.setText(dateFormatted)
                datePicked = dateFormatted
            }, cYear, cMonth, cDay)

        datePickerDialog.show()
    }

    companion object {
        const val EMAIL = "email"
        const val FULLNAME = "fullname"
        const val TITLE_ONE = "title_one"
        const val TITLE_TWO = "title_two"
        const val NO_STR = "no_str"
        const val ROLE = "role"
    }
}