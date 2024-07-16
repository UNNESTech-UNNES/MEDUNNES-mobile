package com.medunnes.telemedicine.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.FragmentProfileBinding
import com.medunnes.telemedicine.ui.main.MainActivity
import com.medunnes.telemedicine.utils.imageBaseUrl
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
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
            cvUserProfile.setOnClickListener(this@ProfileFragment)
            cvFaq.setOnClickListener(this@ProfileFragment)
            cvHint.setOnClickListener(this@ProfileFragment)
            cvLanguange.setOnClickListener(this@ProfileFragment)
            cvDeleteAccount.setOnClickListener(this@ProfileFragment)
        }

        lifecycleScope.launch {
            setViewBasedOnStatus()
            setViewBasedOnUserRole()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private suspend fun setPasienProfile() {
        val userId = viewModel.getUserLoginId()
        viewModel.getPasienByUserLogin(userId)
        viewModel.pasien.observe(viewLifecycleOwner) { data ->
            if (!data.isNullOrEmpty()) {
                data.forEach {
                    with(binding) {
                        tvBeratBadan.text = it.bB.toString() + " kg"
                        tvTinggiBadan.text = it.tB.toString() + " cm"
                        tvUserRole.text = it.user.type

                        if (!it.imgPasien.isNullOrEmpty()) {
                            val imagePath = "${imageBaseUrl()}/${it.imgPasien}"
                            Glide.with(this@ProfileFragment)
                                .load(imagePath)
                                .into(binding.ivUserPicture)
                                .clearOnDetach()
                        }
                    }
                }
            }
        }
    }

    private suspend fun setDokterProfile() {
        val userId = viewModel.getUserLoginId()
        val spesialis = resources.getStringArray(R.array.spesialissasi)
        viewModel.getDokterByUserLogin(userId)
        viewModel.dokter.observe(viewLifecycleOwner) { data ->
            if (!data.isNullOrEmpty()) {
                data.forEach {
                    with(binding) {
                        tvUserName.text = it.user.name
                        tvUserRole.text = spesialis[(it.spesialisId.toInt())-1]

                        if (!it.imgDokter.isNullOrEmpty()) {
                            val imagePath = "${imageBaseUrl()}/${it.imgDokter}"
                            Glide.with(this@ProfileFragment)
                                .load(imagePath)
                                .into(binding.ivUserPicture)
                                .clearOnDetach()
                        }
                    }
                }
            }
        }
    }

    private suspend fun setUserProfile() {
        showProgressBar(true)
        binding.tblProfile.visibility = View.INVISIBLE
        val userId = viewModel.getUserLoginId()
        viewModel.getUserLogin(userId)
        viewModel.user.observe(viewLifecycleOwner) { data ->
            if (!data.isNullOrEmpty()) {
                showProgressBar(false)
                binding.tblProfile.visibility = View.VISIBLE
                data.forEach {
                    with(binding) {
                        tvUserName.text = it.name
                        tvUserEmail.text = it.email
                    }
                }
            } else {
                showProgressBar(false)
                binding.tblProfile.visibility = View.INVISIBLE
            }
        }
    }

    private suspend fun setViewBasedOnUserRole() {
        val userRole = viewModel.getUserRole()
        if (userRole == 1) {
            setUserProfile()
            setDokterProfile()
            binding.tblBadan.visibility = View.GONE
        } else {
            setUserProfile()
            setPasienProfile()
        }
    }

    private suspend fun setViewBasedOnStatus() {
        if (!viewModel.getLoginStatus()) {
            with(binding) {
                cvDeleteAccount.visibility = View.GONE
                cvUserProfile.visibility = View.GONE
            }
        }
    }

    private suspend fun editProfileBasedonRole() {
        if (viewModel.getUserRole() == 1) {
            val intent = Intent(context, ProfileEditDokterActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(context, ProfilePasienEditActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View) {
        with(binding) {
            when(view) {
                cvUserProfile -> lifecycleScope.launch { editProfileBasedonRole() }
                cvFaq -> makeToast("Fitur belum tersedia")
                cvHint -> makeToast("Fitur belum tersedia")
                cvLanguange -> makeToast("Fitur belum tersedia")
                cvDeleteAccount -> {
                    lifecycleScope.launch { viewModel.setLogoutStatus() }
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                else -> makeToast("NO CLICK")
            }
        }
    }
}