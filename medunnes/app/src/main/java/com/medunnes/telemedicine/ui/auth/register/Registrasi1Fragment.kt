package com.medunnes.telemedicine.ui.auth.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.FragmentRegistrasi1Binding

class Registrasi1Fragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegistrasi1Binding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrasi1Binding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnLanjut1.setOnClickListener(this)

        viewModel.getUser(1).observe(viewLifecycleOwner) { data ->
            Log.d("USER", data.toString())
        }
        Log.d("ROLE", "${arguments?.getInt(ROLE)}")

        return root
    }

    private fun bundle() : Bundle {
        val bundle = Bundle()
        with(binding) {
            bundle.putString(Registrasi2Fragment.EMAIL, "${tieEmail.text}")
            bundle.putString(Registrasi2Fragment.FULLNAME, "${tieNamaLengkap.text}")
            bundle.putString(Registrasi2Fragment.TITLE_ONE, "${tieTitelDepan.text}")
            bundle.putString(Registrasi2Fragment.TITLE_TWO, "${tieTitelBelakang.text}")
            bundle.putString(Registrasi2Fragment.NO_STR, "${tieNoStr.text}")
            bundle.putInt(Registrasi2Fragment.ROLE, arguments?.getInt(ROLE, 0) ?: 0)
        }

        return bundle
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.btnLanjut1 -> {
                val registrasi2Fragment = Registrasi2Fragment()
                val fragmentManager = parentFragmentManager

                registrasi2Fragment.arguments = bundle()

                fragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, registrasi2Fragment, Registrasi2Fragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    companion object {
        const val ROLE = "role"
    }
}