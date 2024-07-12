package com.medunnes.telemedicine.ui.pasien.konsultasiPasien

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.FragmentKonsultasiDetailBinding
import com.medunnes.telemedicine.ui.message.MessageActivity
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import com.medunnes.telemedicine.utils.imageBaseUrl

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
                        tvUserStr.text = it.nim.toString()
                        tvUserFullname.text = it.user.name
                        val spsialis = resources.getStringArray(R.array.spesialissasi)
                        tvUserField.text = spsialis[it.spesialisId.toInt()-1]

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
        val dokterId = arguments?.getInt(DOKTER_ID)
        val konsultasiId = arguments?.getInt(KONSULTASI_ID)
        if (dokterId != null) {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra(MessageActivity.DOKTER_ID, dokterId)
            intent.putExtra(MessageActivity.KONSULTASI_ID, konsultasiId)
            startActivity(intent)
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