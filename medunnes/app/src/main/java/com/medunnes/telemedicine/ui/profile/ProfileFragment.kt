package com.medunnes.telemedicine.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.FragmentProfileBinding
import com.medunnes.telemedicine.ui.main.MainActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root


        with(binding) {
            tvProfileEdit.setOnClickListener(this@ProfileFragment)
            cvFaq.setOnClickListener(this@ProfileFragment)
            cvHint.setOnClickListener(this@ProfileFragment)
            cvLanguange.setOnClickListener(this@ProfileFragment)
            cvDeleteAccount.setOnClickListener(this@ProfileFragment)
        }

        lifecycleScope.launch { setProfile() }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    suspend fun setProfile() {
        viewModel.getUserProfile(viewModel.getUserLoginId()).observe(viewLifecycleOwner) { data ->
            data.forEach {
                with(binding) {
                    tvUserName.text = getString(R.string.nama_and_titel, it.fullname, it.titelDepan, it.titelBelakang)
                    tvUserRole.text = it.jenisKelamin
                    tvUserEmail.text = it.email
                    tvUserPraktik.text = it.tempatPraktik
                }
            }
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View) {
        with(binding) {
            when(view) {
                tvProfileEdit -> {
                    val intent = Intent(context, ProfileEditActivity::class.java)
                    startActivity(intent)
                }
                cvFaq -> makeToast("Fitur belum tersedia")
                cvHint -> makeToast("Fitur belum tersedia")
                cvLanguange -> makeToast("Fitur belum tersedia")
                cvDeleteAccount -> {
                    lifecycleScope.launch { viewModel.setLogoutStatus() }
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}