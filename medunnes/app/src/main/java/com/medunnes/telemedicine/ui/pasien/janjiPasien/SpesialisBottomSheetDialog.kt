package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.UserAndDokter
import com.medunnes.telemedicine.databinding.BottomSheetKonsultasiBinding
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import com.medunnes.telemedicine.ui.profile.ProfileViewModel

class SpesialisBottomSheetDialog : BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var binding: BottomSheetKonsultasiBinding
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetKonsultasiBinding.inflate(inflater, container, false)
        binding.root.setBackgroundColor(resources.getColor(R.color.secondary_color))

        with(binding) {
            btnSaraf.setOnClickListener(this@SpesialisBottomSheetDialog)
            btnExpand.setOnClickListener(this@SpesialisBottomSheetDialog)
        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setOnShowListener { it ->
            val bsd = it as BottomSheetDialog
            val bottomSheet =
                bsd.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }


    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(speciality: String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    companion object {
        const val TAG = "SpesialisBottomSheetDialog"
    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                btnDokterUmum -> onItemClickCallback.onItemClicked("Dokter Umum")
                btnKandungan -> onItemClickCallback.onItemClicked("Kandungan")
                btnPsikiater -> onItemClickCallback.onItemClicked("Psikiater")
                btnKulitDanKelamin -> onItemClickCallback.onItemClicked("Kulit dan Kelamin")
                btnSaraf -> onItemClickCallback.onItemClicked("Saraf")
                btnExpand -> dismiss()
                else -> Log.d("CLICK", "no respond")
            }
        }
    }
}