package com.medunnes.telemedicine.ui.registeras

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.medunnes.telemedicine.databinding.ActivityRegisterAsBinding
import com.medunnes.telemedicine.ui.auth.register.RegisterActivity

class RegisterAsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterAsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            ibLoginAsDokter.setOnClickListener(this@RegisterAsActivity)
            ibLoginAsDokter.setOnClickListener(this@RegisterAsActivity)
            btnBack.setOnClickListener(this@RegisterAsActivity)
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
                    val intent = Intent(this@RegisterAsActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
                ibLoginAsPasien -> makeToast(undoneText())
                btnBack -> finish()
            }
        }
    }
}