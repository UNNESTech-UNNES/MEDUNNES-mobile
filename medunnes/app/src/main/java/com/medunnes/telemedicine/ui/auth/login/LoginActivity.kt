package com.medunnes.telemedicine.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.ActivityLoginBinding
import com.medunnes.telemedicine.ui.home.HomeFragment
import com.medunnes.telemedicine.ui.main.MainActivity
import com.medunnes.telemedicine.ui.registeras.RegisterAsActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        with(binding) {
            tvDaftar.setOnClickListener(this@LoginActivity)
            btnLogin.setOnClickListener(this@LoginActivity)
            btnBack.setOnClickListener(this@LoginActivity)
        }
    }

    private fun firebaseLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("USER", user.toString())
                } else {
                    Log.w("Registration", "createUserWithEmail:failure", task.exception)
                }
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
            lifecycleScope.launch {
                try {
                    if (userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
                        showProgressBar()
                        val login = login(userEmail, userPassword)
                        if (login.status) {
                            setUserLoginId(login.user.idUser)
                            if (login.user.type == "dokter") { // Menyimpan type useer ke DataStore
                                setUserLoginRole(1)
                            } else {
                                setUserLoginRole(2)
                            }
                            firebaseLogin(userEmail, userPassword)
                            setLoginStatus() // Menyimpan status login user
                            loginIfSuccess()
                        } else {
                            Toast.makeText(this@LoginActivity,"Email atau password tidak sesuai", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Lengkapi email dan password", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    lifecycleScope.launch {
                        delay(2000)
                        hideProgressBar()
                        Toast.makeText(this@LoginActivity, "Login gagal", Toast.LENGTH_SHORT).show()
                        Log.d("ERROR", e.toString())
                    }
                }
            }
        }
    }

    private fun showProgressBar() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            binding.cvProgressBar.visibility = View.VISIBLE
            binding.btnBack.isClickable = false
            binding.btnLogin.isClickable = false
        }
    }

    private fun hideProgressBar() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.GONE
            binding.cvProgressBar.visibility = View.GONE
            binding.btnBack.isClickable = true
            binding.btnLogin.isClickable = true
        }
    }

    // Mengalihkan user ke home apbila login berhasil
    private suspend fun loginIfSuccess() {
        if (viewModel.getUserStatus()) {
           hideProgressBar()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

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