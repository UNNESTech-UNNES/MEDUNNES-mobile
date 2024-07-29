package com.medunnes.telemedicine.ui.auth.verification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.ActivityEmailVerificationBinding
import com.medunnes.telemedicine.ui.auth.login.LoginActivity
import kotlinx.coroutines.launch

class EmailVerificationActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityEmailVerificationBinding
    private val viewModel by viewModels<VerificationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmailVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSendOtp.setOnClickListener(this)
    }

    private fun verifyEmail() {
        lifecycleScope.launch {
            try {
                val verify = viewModel.verifyEmail(
                    intent.getIntExtra(USER_ID, 0),
                    binding.tieOtp.text.toString()
                )

                if (verify.status) {
                    intentLogin()
                } else {
                    makeToast("Kode OTP salah")
                }
            } catch (e: Exception) {
                makeToast("Verifikasi gagal")
            }
        }
    }

    private fun intentLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View) {
        when(view) {
            binding.btnSendOtp -> verifyEmail()
        }
    }

    companion object {
        const val USER_ID = "user_id"
    }
}