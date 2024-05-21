package com.medunnes.telemedicine.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.Dokter
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.databinding.FragmentRegistrasi3Binding
import com.medunnes.telemedicine.ui.auth.login.LoginActivity

class Registrasi3Fragment : Fragment(),
    View.OnClickListener,
        AdapterView.OnItemSelectedListener
{

    private var _binding: FragmentRegistrasi3Binding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var dataSpinner: String

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

    override fun onClick(view: View?) {
        when(view) {
            binding.tvMasuk -> {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
            binding.btnRegister -> {
                viewModel.isEmailExist(getData(EMAIL)).observe(viewLifecycleOwner) { data ->
                    if ( getData(EMAIL).isNotEmpty() &&
                        getData(FULLNAME).isNotEmpty() &&
                        !binding.tiePassword.text.isNullOrEmpty()) {
                        if ("${binding.tiePassword.text}" == "${binding.tiePasswordConfirmation.text}") {
                            if (data.isNullOrEmpty()) {
                                with(viewModel) {
                                    val user = User(
                                        0,
                                        getData(EMAIL),
                                        "${binding.tiePassword.text}",
                                        getData(FULLNAME),
                                        getData(DATE),
                                        getData(GENDER),
                                        getData(ADDRESS),
                                        "${binding.tieNoTelepon.text}",
                                        arguments?.getInt(ROLE) ?: 2
                                    )
                                    registerDokter(Dokter(
                                        0,
                                        getData(TITLE_ONE),
                                        getData(TITLE_TWO),
                                        getData(NO_STR),
                                        getData(PLACE),
                                        "${binding.tiePendidikan.text}",
                                        dataSpinner,
                                        register(user).toInt()
                                    ))

                                }

                                val intent = Intent(context, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                makeToast("Email sudah terdaftar")
                            }
                        } else {
                            makeToast("Password tidak sesuai")
                        }
                    } else {
                        makeToast("Nama lengkap, email, dan password wajib diisi")
                    }
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
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        dataSpinner = "${parent?.getItemAtPosition(pos)}"
        //getDataSpinner(dataSpinner)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // DO NOTHING
    }
}