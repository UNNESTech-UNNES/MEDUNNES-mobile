package com.medunnes.telemedicine.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.ui.main.MainActivity
import com.medunnes.telemedicine.databinding.ActivityLoginBinding
import com.medunnes.telemedicine.ui.home.HomeFragment
import com.medunnes.telemedicine.ui.registeras.RegisterAsActivity
import kotlinx.coroutines.delay
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

    // Mengatur login user
    private fun setUserLogin() {
        val homeFragment = HomeFragment()
        val bundle = Bundle()
        homeFragment.arguments = bundle

        val userEmail = "${binding.tieUserEmail.text}"
        val userPassword = "${binding.tieUserPassword.text}"

        with(viewModel) {
            try {
                lifecycleScope.launch {
                    if (userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
                        val login = login(userEmail, userPassword)
                        if (login.status) {
                            setUserLoginId(login.user.idUser)
                            if (login.user.type == "dokter") { // Menyimpan type useer ke DataStore
                                setUserLoginRole(1)
                            } else {
                                setUserLoginRole(2)
                            }
                            setLoginStatus() // Menyimpan status login user
                            loginIfSuccess()
                        } else {
                            Toast.makeText(this@LoginActivity,"Email atau password tidak sesuai", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Lengkapi email dan password", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        }
    }

    // Mengalihkan user ke home apbila login berhasil
    private suspend fun loginIfSuccess() {
        while (!viewModel.getUserStatus()) { // Menunggu hingga proses login direspon api
            binding.progressBar.visibility = View.VISIBLE
            delay(1000)
        }

        binding.progressBar.visibility = View.GONE
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)

    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                tvDaftar -> {
                    val intent = Intent(this@LoginActivity, RegisterAsActivity::class.java)
                    startActivity(intent)
                }
                btnLogin -> setUserLogin()
                btnBack -> finish()
            }
        }
    }
}