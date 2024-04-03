package com.medunnes.telemedicine.ui.auth.register

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
import com.medunnes.telemedicine.databinding.FragmentRegistrasi2Binding

class Registrasi2Fragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private var _binding: FragmentRegistrasi2Binding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var dataSpinner: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrasi2Binding.inflate(inflater, container, false)
        val root: View = binding.root

        setSpinner()
        binding.btnLanjut1.setOnClickListener(this)

        val data = arguments?.getString(EMAIL)
        Log.d("DATA", data.toString())
        Log.d("ROLE2","${arguments?.getInt(ROLE)}")

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
            putString(Registrasi3Fragment.NO_STR, getDataBundle(NO_STR))
            putInt(Registrasi3Fragment.ROLE, arguments?.getInt(ROLE)!!)
            putString(Registrasi3Fragment.GENDER, dataSpinner)

            with(binding) {
                putString(Registrasi3Fragment.DATE, "${tieTglLahir.text}")
                putString(Registrasi3Fragment.ADDRESS, "${tieAlamat.text}")
                putString(Registrasi3Fragment.PLACE, "${tieAlamat.text}")
            }

        }

        return bundle
    }

    private fun getDataBundle(data: String) = arguments?.getString(data)

    override fun onClick(view: View?) {
        when(view) {
            binding.btnLanjut1 -> {
                val registrasi3Fragment = Registrasi3Fragment()
                val fragmentManager = parentFragmentManager

                registrasi3Fragment.arguments = bundle()

                fragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, registrasi3Fragment, Registrasi3Fragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    companion object {
        const val EMAIL = "email"
        const val FULLNAME = "fullname"
        const val TITLE_ONE = "title_one"
        const val TITLE_TWO = "title_two"
        const val NO_STR = "no_str"
        const val ROLE = "role"
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        dataSpinner = "${parent?.getItemAtPosition(pos)}"
        getDataSpinner(dataSpinner)
        Toast.makeText(context, dataSpinner, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // DO NOTHING
    }

    private fun getDataSpinner(gender: String): String = gender
}