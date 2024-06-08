package com.medunnes.telemedicine.ui.profile

import android.content.Intent
import android.os.Bundle
import android.os.Environment
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
import kotlinx.coroutines.launch
import java.io.File

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

//        lifecycleScope.launch {
//            if (viewModel.getUserRole() == 1) {
//                setDokterProfile()
//            } else {
//                setProfile()
//            }
//            setViewBasedOnStatus()
//        }

//        lifecycleScope.launch {
//            setProfile()
//            setUserPasienProfile()
//        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    suspend fun setDokterProfile() {
//        viewModel.getUserAndDokter(viewModel.getUserLoginId()).observe(viewLifecycleOwner) { data ->
//            data.forEach {
//                with(binding) {
//                    tvUserName.text = getString(R.string.nama_and_titel, it.dokter.titelDepan, it.user.fullname, it.dokter.titelBelakang)
//                    tvUserRole.text = it.dokter.spesialis
//                    tvUserEmail.text = it.user.email
//                    tvUserPraktik.text = it.dokter.alumni
//                    tblBadan.visibility = View.GONE
//
//                    val path = Environment.getExternalStorageDirectory()
//                    val imageFile = "${File(path, "/Android/data/com.medunnes.telemedicine${it.user.image}")}"
//                    Glide.with(this@ProfileFragment)
//                        .load(imageFile)
//                        .into(ivUserPicture)
//                        .clearOnDetach()
//                }
//            }
//        }
    }

    private suspend fun setProfile() {
//        viewModel.getUserProfile(viewModel.getUserLoginId()).observe(viewLifecycleOwner) { data ->
//            data.forEach {
//                with(binding) {
//                    tvUserName.text = it.fullname
//                    tvUserEmail.text = it.email
//                    tblTempatPraktik.visibility = View.GONE

//                    if (!it.image.isNullOrEmpty()) {
//                        val path = Environment.getExternalStorageDirectory()
//                        val imageFile = "${File(path, "/Android/data/com.medunnes.telemedicine${it.image}")}"
//                        Glide.with(this@ProfileFragment)
//                            .load(imageFile)
//                            .into(ivUserPicture)
//                            .clearOnDetach()
//                    }
//                }
//            }
//        }
//        setUserPasienProfile()
    }

    private suspend fun setPasienProfile() {
        val userId = viewModel.getUserLoginId()
        viewModel.getPasienByUser(userId).observe(viewLifecycleOwner) { data ->
            data.forEach {
                with(binding) {
                    tvTinggiBadan.text = it.tb.toString()
                    tvBeratBadan.text = it.bb.toString()
                }
            }

        }
    }

    private suspend fun setUserPasienProfile() {
        val userId = viewModel.getUserLoginId()
        var name = ""
        var email = ""
        var role = ""
        var tb = ""
        var bb = ""
        var img = ""
        viewModel.getUserProfile(userId).observe(viewLifecycleOwner) { data ->
            data.forEach {
                name = it.fullname
                email = it.fullname
                role = it.type
            }
        }

        viewModel.getPasienByUser(userId).observe(viewLifecycleOwner) { data ->
            data.forEach {
                tb = it.tb.toString()
                bb = it.bb.toString()
            }
        }

        viewModel.getUserPasienProfile(userId).data.forEach {
            tb = it.tB.toString()
            bb = it.bB.toString()
        }

        with(binding) {
            tvUserName.text = name
            tvUserRole.text = role
            tvUserEmail.text = email
            tvTinggiBadan.text = tb
            tvBeratBadan.text = bb
            tblTempatPraktik.visibility = View.GONE

            if (!img.isNullOrEmpty()) {
                val path = Environment.getExternalStorageDirectory()
                val imageFile = "${File(path, "/Android/data/com.medunnes.telemedicine${img}")}"
                Glide.with(this@ProfileFragment)
                    .load(imageFile)
                    .into(ivUserPicture)
                    .clearOnDetach()
            }
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

    private fun makeToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View) {
        with(binding) {
            when(view) {
                cvUserProfile -> {
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