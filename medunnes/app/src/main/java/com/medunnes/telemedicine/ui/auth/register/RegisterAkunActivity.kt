package com.medunnes.telemedicine.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.ActivityRegisterAkunBinding
import com.medunnes.telemedicine.ui.auth.verification.EmailVerificationActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterAkunActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityRegisterAkunBinding
    private var dataSpinner: String = ""

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setSpinner()
        with(binding) {
            btnRegister.setOnClickListener(this@RegisterAkunActivity)
            btnBack.setOnClickListener(this@RegisterAkunActivity)
        }
    }

    private suspend fun registerUser() {
        with(binding) {
            val userId: Long
            val pasienId: Long
            try {
                showProgressBar()
                val email = "${tieEmail.text}"
                val password = "${tiePassword.text}"
                val userRegister = viewModel.registerAPI(
                    "${tieNamaLengkap.text}",
                    email,
                    password,
                    "pasien"
                )
                userId = userRegister.data[0].idUser.toLong()

                val pasienInsert = viewModel.insertPasien(
                    userId,
                    tieNik.text.toString().toLong(),
                    null,
                    dataSpinner,
                    "${tieAlamat.text}",
                    "${tieNoTelepon.text}",
                    tieTb.text.toString().toInt(),
                    tieBb.text.toString().toInt(),
                    "active",
                )

                pasienId = pasienInsert.data[0].idPasien
                val pasienTambahanInsert = viewModel.insertPasienTambahan(
                    pasienId,
                    "${tieNamaLengkap.text}",
                    tieTb.text.toString().toInt(),
                    tieBb.text.toString().toInt(),
                    dataSpinner,
                    "Diri sendiri"
                )

                firebaseRegister(email, password)
                intentVerification(pasienTambahanInsert.status, userId.toInt())
            } catch (e: Exception) {
                lifecycleScope.launch {
                    delay(2000)
                    hideProgressBar()
                    Toast.makeText(this@RegisterAkunActivity, "Register gagal", Toast.LENGTH_SHORT).show()
                    Log.d("ERROR", e.message.toString())
                }
            }
        }
    }

    private fun showProgressBar() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            binding.cvProgressBar.visibility = View.VISIBLE
            binding.btnBack.isClickable = false
            binding.btnRegister.isClickable = false
        }
    }

    private fun hideProgressBar() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.GONE
            binding.cvProgressBar.visibility = View.GONE
            binding.btnBack.isClickable = true
            binding.btnRegister.isClickable = true
        }
    }

    // Mengalihkan user ke home apbila login berhasil
    private fun intentVerification(pasienTambahanIns: Boolean, id: Int) {
        if (pasienTambahanIns) {
            binding.progressBar.visibility = View.GONE
            binding.cvProgressBar.visibility = View.GONE
            val intent = Intent(this@RegisterAkunActivity, EmailVerificationActivity::class.java)
            intent.putExtra(EmailVerificationActivity.USER_ID, id)
            startActivity(intent)
        }

    }

    private fun firebaseRegister(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("USER", user.toString())
                } else {
                    Log.w("Registration", "createUserWithEmail:failure", task.exception)
                }
            }
    }

    private fun getDataSpinner(gender: String): String = gender

    private fun setSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.jenis_kelamin,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerKelamin.adapter = arrayAdapter
        }
        binding.spinnerKelamin.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        val genderPicked = "${parent?.getItemAtPosition(pos)}"
        dataSpinner = if (genderPicked == "Laki-laki") "L" else "P"
        getDataSpinner(dataSpinner)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // DO NOTHING
    }

    companion object {
        const val ROLE = "role"
    }

    override fun onClick(view: View) {
        with(binding) {
            when(view) {
                btnRegister -> lifecycleScope.launch { registerUser() }
                btnBack -> finish()
                else -> { /* DO NOTHING */ }
            }
        }
    }
}