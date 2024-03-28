package com.medunnes.telemedicine.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.ui.main.MainActivity
import com.medunnes.telemedicine.databinding.ActivityLoginBinding
import com.medunnes.telemedicine.ui.home.HomeFragment
import com.medunnes.telemedicine.ui.registeras.RegisterAsActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

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
                    val homeFragment = HomeFragment()
                    val bundle = Bundle()
                    homeFragment.arguments = bundle

                    val userEmail = "${binding.tieUserEmail.text}"
                    val userPassword = "${binding.tieUserPassword.text}"
                    viewModel.login(userEmail, userPassword).observe(this@LoginActivity) { data ->
                        if (!data.isNullOrEmpty()) {
                            lifecycleScope.launch {
                                viewModel.setLoginStatus()
                                data.forEach {
                                    viewModel.setUserLoginId(it.id)
                                    viewModel.setUserLoginRole(it.role)
                                }
                            }
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@LoginActivity,
                                "Email atau password tidak sesuai", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                btnBack -> finish()
            }
        }
    }
}