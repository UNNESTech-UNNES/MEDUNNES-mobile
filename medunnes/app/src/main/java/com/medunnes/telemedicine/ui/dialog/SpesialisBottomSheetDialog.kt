package com.medunnes.telemedicine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.databinding.BottomSheetKonsultasiBinding

class SpesialisBottomSheetDialog : BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var binding: BottomSheetKonsultasiBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetKonsultasiBinding.inflate(inflater, container, false)
        @Suppress("DEPRECATION")
        binding.root.setBackgroundColor(resources.getColor(R.color.secondary_color))

        itemClicked()

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
        fun onItemClicked(speciality: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private fun itemClicked() {
        with(binding) {
            btnDokterUmum.setOnClickListener(this@SpesialisBottomSheetDialog)
            btnKandungan.setOnClickListener(this@SpesialisBottomSheetDialog)
            btnPsikiater.setOnClickListener(this@SpesialisBottomSheetDialog)
            btnKulitDanKelamin.setOnClickListener(this@SpesialisBottomSheetDialog)
            btnAnak.setOnClickListener(this@SpesialisBottomSheetDialog)
            btnSaraf.setOnClickListener(this@SpesialisBottomSheetDialog)
            btnJantung.setOnClickListener(this@SpesialisBottomSheetDialog)
            btnBedah.setOnClickListener(this@SpesialisBottomSheetDialog)
            btnPenyakitDalam.setOnClickListener(this@SpesialisBottomSheetDialog)
            btnMata.setOnClickListener(this@SpesialisBottomSheetDialog)
            btnTht.setOnClickListener(this@SpesialisBottomSheetDialog)
            btnGigi.setOnClickListener(this@SpesialisBottomSheetDialog)
            btnExpand.setOnClickListener(this@SpesialisBottomSheetDialog)
        }
    }


    companion object {
        const val TAG = "SpesialisBottomSheetDialog"
    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                btnDokterUmum -> onItemClickCallback.onItemClicked(0)
                btnKandungan -> onItemClickCallback.onItemClicked(1)
                btnPsikiater -> onItemClickCallback.onItemClicked(2)
                btnKulitDanKelamin -> onItemClickCallback.onItemClicked(3)
                btnAnak -> onItemClickCallback.onItemClicked(4)
                btnSaraf -> onItemClickCallback.onItemClicked(5)
                btnJantung -> onItemClickCallback.onItemClicked(6)
                btnBedah -> onItemClickCallback.onItemClicked(7)
                btnPenyakitDalam -> onItemClickCallback.onItemClicked(8)
                btnMata -> onItemClickCallback.onItemClicked(9)
                btnTht -> onItemClickCallback.onItemClicked(10)
                btnGigi -> onItemClickCallback.onItemClicked(11)
                btnExpand -> dismiss()
                else -> Log.d("CLICK", "no respond")
            }
        }
    }
}