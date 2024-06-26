package com.medunnes.telemedicine.ui.pasien.konsultasiPasien

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.FragmentKonsultasiDetailBinding
import com.medunnes.telemedicine.ui.message.MessageActivity
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import com.medunnes.telemedicine.utils.imageBaseUrl
import kotlinx.coroutines.launch

class KonsultasiDetailFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentKonsultasiDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentKonsultasiDetailBinding.inflate(inflater, container, false)
        setProfileData()

        binding.btnKonsultasiNow.setOnClickListener(this)

        return binding.root
    }

    private fun setProfileData() {
        val dokterId = arguments?.getInt(DOKTER_ID)
        if (dokterId != null) {
            viewModel.getDokterById(dokterId)
            viewModel.dokter.observe(viewLifecycleOwner) { dokter ->
                dokter.forEach {
                    with(binding) {
                        tvUserFullname.text =
                            getString(R.string.nama_and_titel, it.titleDepan, it.namaDokter, it.titleBelakang)
                        tvUserEducation.text = it.alumni
                        tvUserExperience.text = it.tempatKerja
                        tvUserLocation.text = it.tempatKerja
                        tvUserStr.text = it.noReg.toString()

                        val spsialis = resources.getStringArray(R.array.spesialissasi)
                        tvUserField.text = spsialis[it.spesialisId.toInt()]

                        if (!it.imgDokter.isNullOrEmpty()) {
                            val imagePath = "${imageBaseUrl()}/${it.imgDokter}"
                            Glide.with(this@KonsultasiDetailFragment)
                                .load(imagePath)
                                .into(binding.ivUserPicture)
                                .clearOnDetach()
                        }
                    }
                }
            }
        }
    }

    private fun intentMessage() {
        var id = 0
        lifecycleScope.launch {
            val uid = viewModel.getUserLoginId()
            val role = viewModel.getUserRole()
            if (role == 1) {

            } else {
                val dokterId = arguments?.getInt(DOKTER_ID)
                val konsultasiId = arguments?.getInt(KONSULTASI_ID)
                if (dokterId != null) {
                    val intent = Intent(context, MessageActivity::class.java)
                    intent.putExtra(MessageActivity.DOKTER_ID, dokterId)
                    intent.putExtra(MessageActivity.KONSULTASI_ID, konsultasiId)
                    startActivity(intent)
                }

            }
        }
    }

    companion object {
        const val DOKTER_ID = "dokter_id"
        const val KONSULTASI_ID = "konsultasi_id"
    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                btnKonsultasiNow -> intentMessage()
            }
        }
    }
}