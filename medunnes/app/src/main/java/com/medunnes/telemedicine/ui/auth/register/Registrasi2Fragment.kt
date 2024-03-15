package com.medunnes.telemedicine.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.databinding.FragmentRegistrasi2Binding

class Registrasi2Fragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegistrasi2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRegistrasi2Binding.inflate(inflater, container, false)
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
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.btnLanjut1 -> {
                val registrasi3Fragment = Registrasi3Fragment()
                val fragmentManager = parentFragmentManager
                fragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, registrasi3Fragment, Registrasi3Fragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }
}