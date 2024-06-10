package com.medunnes.telemedicine.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.response.UserResponse
import com.medunnes.telemedicine.databinding.FragmentRegistrasi3Binding
import com.medunnes.telemedicine.ui.auth.login.LoginActivity
import kotlinx.coroutines.launch

class Registrasi3Fragment : Fragment(),
    View.OnClickListener,
        AdapterView.OnItemSelectedListener
{

    private var _binding: FragmentRegistrasi3Binding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var dataSpinner = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrasi3Binding.inflate(inflater, container, false)
        val root: View = binding.root

        setSpinner()
        with(binding) {
            tvMasuk.setOnClickListener(this@Registrasi3Fragment)
            btnRegister.setOnClickListener(this@Registrasi3Fragment)
        }

        return root
    }

    private fun makeToast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    private fun getData(data: String): String ="${arguments?.getString(data)}"

    private fun inputValidation(): Boolean {
        with(binding) {
            return ((getData(EMAIL).isNotEmpty()
                    && getData(FULLNAME).isNotEmpty()
                    && getData(DATE).isNotEmpty()
                    && getData(GENDER).isNotEmpty()
                    && getData(ADDRESS).isNotEmpty())
                    && getData(TITLE_ONE).isNotEmpty()
                    && getData(TITLE_TWO).isNotEmpty()
                    && arguments?.getLong(NO_STR) != null
                    && getData(PLACE).isNotEmpty()
                    && arguments?.getInt(ROLE) != null
                    && getData(GRADUATE_PLACE).isNotEmpty())
                    && !tiePendidikan.text.isNullOrEmpty()
                    && !tieNoTelepon.text.isNullOrEmpty()
                    && !tiePassword.text.isNullOrEmpty()
                    && !tiePasswordConfirmation.text.isNullOrEmpty()
        }
    }

    private fun passwordValidation(): Boolean {
        return "${binding.tiePassword.text}" == "${binding.tiePasswordConfirmation.text}"
    }

    private suspend fun insertUser(): UserResponse {
        val registerUser = viewModel.registerAPI(
            getData(FULLNAME),
            getData(EMAIL),
            "${binding.tiePassword.text}",
            "dokter"
        )
        return registerUser
    }

    private suspend fun insertDokter() {
        var userId = 0
        if (inputValidation()) {
            if (passwordValidation()) {
                insertUser().data.forEach {
                    userId = it.idUser
                }

                viewModel.insertDokter(
                    userId,
                    dataSpinner,
                    getData(TITLE_ONE),
                    getData(FULLNAME),
                    getData(TITLE_TWO),
                    null,
                    getData(ADDRESS),
                    "${binding.tieNoTelepon.text}",
                    getData(PLACE),
                    getData(GRADUATE_PLACE),
                    getData(DATE),
                    "${binding.tiePendidikan.text}",
                    arguments?.getLong(NO_STR)!!,
                    getData(GENDER),
                    "pending"
                )

                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            } else {
                makeToast("Password tidak sesuai")
            }
        } else {
            makeToast("Lengkapi data terlebih dahulu")
        }
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.tvMasuk -> {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
            binding.btnRegister -> {
                try {
                    lifecycleScope.launch { insertDokter() }
                } catch (e: Exception) {
                    makeToast("Ada kesalahan. Coba lagi nanti")
                    Log.d("REGISTER DOKTER ERROR", e.message.toString())
                }
            }
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
        const val TITLE_ONE = "title_one"
        const val TITLE_TWO = "title_two"
        const val NO_STR = "no_str"
        const val DATE = "date"
        const val GENDER = "gender"
        const val ADDRESS = "address"
        const val PLACE = "place"
        const val ROLE = "role"
        const val GRADUATE_PLACE = "graduate_place"
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        Toast.makeText(context, (pos+1).toString(), Toast.LENGTH_SHORT).show()
        dataSpinner = pos+1
        //getDataSpinner(dataSpinner)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // DO NOTHING
    }
}