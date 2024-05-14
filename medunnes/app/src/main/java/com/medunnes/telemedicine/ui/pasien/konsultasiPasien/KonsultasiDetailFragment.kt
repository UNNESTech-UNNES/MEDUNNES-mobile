package com.medunnes.telemedicine.ui.pasien.konsultasiPasien

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.databinding.FragmentKonsultasiBinding
import com.medunnes.telemedicine.databinding.FragmentKonsultasiDetailBinding
import com.medunnes.telemedicine.ui.pasien.LayananPasienActivity
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import com.medunnes.telemedicine.ui.profile.ProfileViewModel

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
        arguments?.getInt(DOKTER_ID)?.let {
            viewModel.getDokterByUid(it).observe(viewLifecycleOwner) { data ->
                data.forEach {
                    with(binding) {
                        tvUserFullname.text = getString(R.string.nama_and_titel,
                            it.dokter.titelDepan, it.user.fullname, it.dokter.titelBelakang)
                        tvUserField.text = it.dokter.tempatPraktik
                        tvUserExperience.text = it.user.tanggalLahir
                        tvUserEducation.text = it.user.jenisKelamin
                        tvUserLocation.text = it.dokter.tempatPraktik
                        tvUserStr.text = it.dokter.noStr
                    }
                }
            }
        }
    }

    companion object {
        const val DOKTER_ID = "dokter_id"
    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                btnKonsultasiNow -> Toast.makeText(context, "Fitur belum tersedia", Toast.LENGTH_SHORT).show()
            }
        }
    }
}