package com.medunnes.telemedicine.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.response.UserResponse
import com.medunnes.telemedicine.databinding.FragmentRegistrasi3Binding
import com.medunnes.telemedicine.ui.auth.login.LoginActivity
import com.medunnes.telemedicine.ui.auth.verification.EmailVerificationActivity
import kotlinx.coroutines.launch

class Registrasi2Fragment : Fragment(),
    View.OnClickListener,
        AdapterView.OnItemSelectedListener
{
    private var _binding: FragmentRegistrasi3Binding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var dataSpinner = 0
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrasi3Binding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()

        setSpinner()
        with(binding) {
            tvMasuk.setOnClickListener(this@Registrasi2Fragment)
            btnRegister.setOnClickListener(this@Registrasi2Fragment)
        }

        return root
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

    private fun makeToast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    private fun getData(data: String): String ="${arguments?.getString(data)}"

    private fun inputValidation(): Boolean {
        with(binding) {
            return ((getData(EMAIL).isNotEmpty()
                    && getData(FULLNAME).isNotEmpty()
                    && getData(GENDER).isNotEmpty()
                    && getData(ADDRESS).isNotEmpty())
                    && arguments?.getLong(NIM) != null
                    && arguments?.getInt(ROLE) != null
                    && !tieNoTelepon.text.isNullOrEmpty()
                    && !tiePassword.text.isNullOrEmpty()
                    && !tiePasswordConfirmation.text.isNullOrEmpty())
        }
    }

    private fun passwordValidation(): Boolean {
        return "${binding.tiePassword.text}" == "${binding.tiePasswordConfirmation.text}"
    }

    private fun insertUser() {
        showProgressBar()
        lifecycleScope.launch {
            try {
                val email = getData(EMAIL)
                val password = "${binding.tiePassword.text}"
                val registerUser = viewModel.registerAPI(
                    getData(FULLNAME),
                    email,
                    password,
                    "dokter"
                )
                firebaseRegister(email, password)
                insertDokter(registerUser)
            } catch (e: Exception) {
                hideProgressBar()
                Toast.makeText(context, "Register gagal", Toast.LENGTH_SHORT).show()
                Log.d("ERROR", e.message.toString())
            }
        }
    }

    private suspend fun insertDokter(registerUser: UserResponse) {
        val userId: Int
        if (inputValidation()) {
            if (passwordValidation()) {
                userId = registerUser.data[0].idUser
                val dokterIns = viewModel.insertDokter(
                    userId.toLong(),
                    1,
                    dataSpinner.toLong(),
                    null,
                    getData(ADDRESS),
                    "${binding.tieNoTelepon.text}",
                    arguments?.getLong(NIM)!!,
                    getData(GENDER),
                    "pending"
                )
                intentVerification(dokterIns.status, userId)
            } else {
                makeToast("Password tidak sesuai")
            }
        } else {
            makeToast("Lengkapi data terlebih dahulu")
        }
    }

    private fun intentVerification(dokterIns: Boolean, id: Int) {
        if (dokterIns) {
            hideProgressBar()
            val intent = Intent(context, EmailVerificationActivity::class.java)
            intent.putExtra(EmailVerificationActivity.USER_ID, id)
            startActivity(intent)
        }
    }

    private fun showProgressBar() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            binding.cvProgressBar.visibility = View.VISIBLE
            binding.btnRegister.isClickable = false
        }
    }

    private fun hideProgressBar() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.GONE
            binding.cvProgressBar.visibility = View.GONE
            binding.btnRegister.isClickable = true
        }
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.tvMasuk -> {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
            binding.btnRegister -> insertUser()
        }
    }

    private fun setSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spesialissasi,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSpesialisasi.adapter = arrayAdapter
        }
        binding.spinnerSpesialisasi.onItemSelectedListener = this
    }

    companion object {
        const val EMAIL = "email"
        const val FULLNAME = "fullname"
        const val NIM = "nim"
        const val GENDER = "gender"
        const val ADDRESS = "address"
        const val ROLE = "role"
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        Toast.makeText(context, (pos+1).toString(), Toast.LENGTH_SHORT).show()
        dataSpinner = pos+1
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // DO NOTHING
    }
}