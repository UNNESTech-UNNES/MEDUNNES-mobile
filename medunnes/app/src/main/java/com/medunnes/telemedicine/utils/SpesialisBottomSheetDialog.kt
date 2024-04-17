package com.medunnes.telemedicine.utils

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.databinding.BottomSheetKonsultasiBinding

class SpesialisBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetKonsultasiBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetKonsultasiBinding.inflate(inflater, container, false)
        binding.root.setBackgroundColor(resources.getColor(R.color.secondary_color))
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

    companion object {
        const val TAG = "SpesialisBottomSheetDialog"
    }
}