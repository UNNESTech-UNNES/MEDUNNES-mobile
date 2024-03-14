package com.medunnes.telemedicine.ui.loginas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.databinding.ActivityLoginAsBinding
import com.medunnes.telemedicine.ui.auth.login.LoginActivity

class LoginAsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginAsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            ibLoginAsDokter.setOnClickListener(this@LoginAsActivity)
            ibLoginAsDokter.setOnClickListener(this@LoginAsActivity)
        }
    }

    private fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun undoneText(): String = "Fitur belum tersedia"

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                ibLoginAsDokter -> {
                    val intent = Intent(this@LoginAsActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
                ibLoginAsPasien -> makeToast(undoneText())
            }
        }
    }
}