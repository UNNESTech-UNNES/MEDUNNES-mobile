package com.medunnes.telemedicine.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.medunnes.telemedicine.ui.main.MainActivity
import com.medunnes.telemedicine.databinding.FragmentRegistrasi3Binding
import com.medunnes.telemedicine.ui.auth.login.LoginActivity

class Registrasi3Fragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegistrasi3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrasi3Binding.inflate(inflater, container, false)
        val root: View = binding.root

        with(binding) {
            tvMasuk.setOnClickListener(this@Registrasi3Fragment)
            btnRegister.setOnClickListener(this@Registrasi3Fragment)
        }

        return root
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.tvMasuk -> {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
            binding.btnRegister -> {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}