package com.medunnes.telemedicine.ui.auth.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.databinding.FragmentRegistrasi1Binding
import kotlinx.coroutines.launch

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


        return root
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.btnLanjut1 -> {
                val registrasi2Fragment = Registrasi2Fragment()
                val fragmentManager = parentFragmentManager
                fragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, registrasi2Fragment, Registrasi2Fragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }
}