package com.medunnes.telemedicine.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.ui.main.MainActivity
import com.medunnes.telemedicine.databinding.FragmentRegistrasi3Binding
import com.medunnes.telemedicine.ui.auth.login.LoginActivity

class Registrasi3Fragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegistrasi3Binding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrasi3Binding.inflate(inflater, container, false)
        val root: View = binding.root

        with(binding) {
            tvMasuk.setOnClickListener(this@Registrasi3Fragment)
            btnRegister.setOnClickListener(this@Registrasi3Fragment)
        }

        val data = arguments?.getString(Registrasi2Fragment.EMAIL)
        Log.d("DATA", data.toString())

        return root
    }

    private fun getData(data: String): String ="${arguments?.getString(data)}"

    override fun onClick(view: View?) {
        when(view) {
            binding.tvMasuk -> {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
            binding.btnRegister -> {
                val intent = Intent(context, MainActivity::class.java)

                viewModel.register(User(
                    0,
                    getData(EMAIL),
                    "${binding.tiePassword.text}",
                    getData(FULLNAME),
                    getData(TITLE_ONE),
                    getData(TITLE_TWO),
                    getData(NO_STR),
                    getData(DATE),
                    "Laki-laki",
                    getData(ADDRESS),
                    getData(PLACE),
                    "${binding.tieNoTelepon.text}"
                ))

                startActivity(intent)
            }
        }
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
    }
}