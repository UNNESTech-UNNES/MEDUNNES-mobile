package com.medunnes.telemedicine.ui.auth.register

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.ActivityRegisterAkunBinding
import com.medunnes.telemedicine.ui.auth.login.LoginActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterAkunActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityRegisterAkunBinding
    private var dataSpinner: String = ""
    private var datePicked: String = ""

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSpinner()
        with(binding) {
            btnRegister.setOnClickListener(this@RegisterAkunActivity)
            btnBack.setOnClickListener(this@RegisterAkunActivity)
            tilTglLahir.setEndIconOnClickListener { showDatePicker() }
        }
    }

    private suspend fun registerUser() {
        with(binding) {

            var userId: Long = 0
            try {
                val userRegister = viewModel.registerAPI(
                    "${tieNamaLengkap.text}",
                    "${tieEmail.text}",
                    "${tiePassword.text}",
                    "pasien"
                )

                userRegister.data.forEach {
                    userId = it.idUser.toLong()
                }

                viewModel.insertPasien(
                    userId,
                    tieNik.text.toString().toLong(),
                    "${tieNamaLengkap.text}",
                    null,
                    dataSpinner,
                    "${tieAlamat.text}",
                    "${tieNoTelepon.text}",
                    tieTb.text.toString().toInt(),
                    tieBb.text.toString().toInt(),
                    "active",
                )
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }

        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val cYear = calendar.get(Calendar.YEAR)
        val cMonth = calendar.get(Calendar.MONTH)
        val cDay = calendar.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(
            this, { _, year, month, day ->
                calendar.set(year, month, day)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.tieTglLahir.setText(dateFormat.format(calendar.time))
                datePicked = "${calendar.time}"

            }, cYear, cMonth, cDay)

        datePickerDialog.show()
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

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                btnRegister -> {
                    lifecycleScope.launch { registerUser() }

                    val intent = Intent(this@RegisterAkunActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
                btnBack -> finish()
            }
        }
    }
}