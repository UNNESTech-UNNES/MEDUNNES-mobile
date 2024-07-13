package com.medunnes.telemedicine.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.FragmentRegistrasi1Binding

class Registrasi1Fragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener{

    private var _binding: FragmentRegistrasi1Binding? = null
    private val binding get() = _binding!!
    private lateinit var dataSpinner: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrasi1Binding.inflate(inflater, container, false)
        val root: View = binding.root

        setSpinner()
        binding.btnLanjut1.setOnClickListener(this)

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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        dataSpinner = if (pos == 1) "P" else "L"
        getDataSpinner(dataSpinner)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // DO NOTHING
    }

    private fun getDataSpinner(gender: String): String = gender

    private fun bundle() : Bundle {
        val bundle = Bundle()
        with(binding) {
            bundle.putString(Registrasi2Fragment.EMAIL, "${tieEmail.text}")
            bundle.putString(Registrasi2Fragment.FULLNAME, "${tieNamaLengkap.text}")
            bundle.putLong(Registrasi2Fragment.NIM, tieNim.text.toString().toLong())
            bundle.putString(Registrasi2Fragment.ADDRESS, "${tieAlamat.text}")
            bundle.putString(Registrasi2Fragment.GENDER, dataSpinner)
            bundle.putInt(Registrasi2Fragment.ROLE, arguments?.getInt(ROLE, 0) ?: 0)
        }

        return bundle
    }

    private fun inputValidation(): Boolean {
        with(binding) {
            return (
                    !tieEmail.text.isNullOrEmpty()
                    && !tieNamaLengkap.text.isNullOrEmpty()
                    && !tieNim.text.isNullOrEmpty()
                    && spinnerKelamin.isNotEmpty()
                    && !tieAlamat.text.isNullOrEmpty()
                    )
        }
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.btnLanjut1 -> {
                val registrasi2Fragment = Registrasi2Fragment()
                val fragmentManager = parentFragmentManager

                if (inputValidation()) {
                    registrasi2Fragment.arguments = bundle()
                    fragmentManager.beginTransaction().apply {
                        replace(R.id.frame_container, registrasi2Fragment, Registrasi2Fragment::class.java.simpleName)
                        addToBackStack(null)
                        commit()
                    }
                } else {
                    Toast.makeText(context, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val ROLE = "role"
    }
}