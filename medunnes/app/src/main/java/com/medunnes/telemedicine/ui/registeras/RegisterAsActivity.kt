package com.medunnes.telemedicine.ui.registeras

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.medunnes.telemedicine.databinding.ActivityRegisterAsBinding
import com.medunnes.telemedicine.ui.auth.register.RegisterActivity
import com.medunnes.telemedicine.ui.auth.register.RegisterAkunActivity

class RegisterAsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterAsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            ibLoginAsDokter.setOnClickListener(this@RegisterAsActivity)
            ibLoginAsPasien.setOnClickListener(this@RegisterAsActivity)
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
                    intent.putExtra(RegisterActivity.ROLE, 1)

                    startActivity(intent)
                }
                ibLoginAsPasien -> {
                    val intent = Intent(this@RegisterAsActivity, RegisterAkunActivity::class.java)
                    intent.putExtra(RegisterAkunActivity.ROLE, 2)
                    startActivity(intent)
                }
                btnBack -> finish()
            }
        }
    }
}