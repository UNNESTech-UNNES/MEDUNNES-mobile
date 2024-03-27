package com.medunnes.telemedicine.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.databinding.ActivityRegisterAkunBinding
import com.medunnes.telemedicine.ui.auth.login.LoginActivity

class RegisterAkunActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterAkunBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnRegister.setOnClickListener(this@RegisterAkunActivity)
            btnBack.setOnClickListener(this@RegisterAkunActivity)
        }

        registerUser()
    }

    private fun registerUser() {
        with(binding) {
         viewModel.register(User(
             0,
             "${tieEmail.text}",
             "${tiePassword.text}",
             "${tieNamaLengkap.text}",
             "27/04/09",
             "Laki-laki",
             "${tieAlamat.text}",
             "${tieNoTelepon.text}",
             intent.getIntExtra(ROLE, 0)
         ))
        }
    }

    companion object {
        const val ROLE = "role"
    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                btnRegister -> {
                    registerUser()

                    val intent = Intent(this@RegisterAkunActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
                btnBack -> finish()
            }
        }
    }
}