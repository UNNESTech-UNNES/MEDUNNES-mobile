package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.FragmentBuatJanjiDokterBinding
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel

class BuatJanjiDokterFragment : Fragment() {
    private var _binding: FragmentBuatJanjiDokterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBuatJanjiDokterBinding.inflate(inflater, container, false)
        val doctorId = arguments?.getInt(DOCTOR_ID)
        Log.d("ID", "$doctorId")
        if (doctorId != null) setDoctorProfile(doctorId)

        return binding.root
    }

    private fun setDoctorProfile(doctorId: Int) {
        viewModel.getDokterById(doctorId).observe(viewLifecycleOwner) { data ->
            data.forEach {
                with(binding) {
                    tvDoctorName.text = getString(R.string.nama_and_titel,
                        it.dokter.titelDepan, it.user.fullname, it.dokter.titelBelakang
                    )
                    tvDoctorExperience.text = it.dokter.pendidikan
                    tvDoctorSpeciality.text = it.dokter.spesialis
                }
            }

        }
    }

    companion object {
        const val DOCTOR_ID = "doctor_id"
    }
}