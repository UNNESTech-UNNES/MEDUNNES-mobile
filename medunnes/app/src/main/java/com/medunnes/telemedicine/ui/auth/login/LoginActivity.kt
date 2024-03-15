package com.medunnes.telemedicine.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.medunnes.telemedicine.MainActivity
import com.medunnes.telemedicine.databinding.ActivityLoginBinding
import com.medunnes.telemedicine.ui.registeras.RegisterAsActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            tvDaftar.setOnClickListener(this@LoginActivity)
            btnLogin.setOnClickListener(this@LoginActivity)
            btnBack.setOnClickListener(this@LoginActivity)
        }
    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                tvDaftar -> {
                    val intent = Intent(this@LoginActivity, RegisterAsActivity::class.java)
                    startActivity(intent)
                }
                btnLogin -> {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                btnBack -> finish()
            }
        }
    }
}