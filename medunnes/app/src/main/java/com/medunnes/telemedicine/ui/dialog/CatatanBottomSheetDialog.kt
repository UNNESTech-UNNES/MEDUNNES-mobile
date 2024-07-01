package com.medunnes.telemedicine.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.medunnes.telemedicine.data.response.CatatanDataItem
import com.medunnes.telemedicine.databinding.BottomSheetCatatanBinding

class CatatanBottomSheetDialog : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetCatatanBinding
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onBtnSimpanCatatanClicked(catatan: CatatanDataItem)
        fun onBtnAkhiriPesanClicked()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetCatatanBinding.inflate(inflater, container, false)
        setCatatan()
        setViewbasedOnStatus()
        btnCatatanClicked()
        btnAkhiriSesiClicked()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setOnShowListener { it ->
            val cbsd = it as BottomSheetDialog
            val bottomSheet =
                cbsd.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return super.onCreateDialog(null)
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDestroy()
        super.onDismiss(dialog)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private fun setCatatan() {
        val idCatatan = arguments?.getInt(ID_CATATAN)
        if (idCatatan != null) {
            val gejala = arguments?.getString(GEJALA)
            val diagnosis = arguments?.getString(DIAGNOSIS)
            val catatan = arguments?.getString(CATATAN)

            binding.tieGejala.setText(gejala)
            binding.tieDiagnosis.setText(diagnosis)
            binding.tieCatatan.setText(catatan)

            binding.btnAkhiriSesi.visibility = View.VISIBLE
        }
    }

    private fun btnCatatanClicked() {
        binding.btnCatatanSend.setOnClickListener {
            onItemClickCallback.onBtnSimpanCatatanClicked(CatatanDataItem(
                "",
                0,
                "${binding.tieDiagnosis.text}",
                "${binding.tieCatatan.text}",
                "",
                0,
                "${binding.tieGejala.text}"
            ))
        }
    }

    private fun btnAkhiriSesiClicked() {
        binding.btnAkhiriSesi.setOnClickListener {
            onItemClickCallback.onBtnAkhiriPesanClicked()
        }
    }

    private fun setViewbasedOnStatus() {
        val status = arguments?.getString(STATUS)
        if (status == "berakhir") {
            binding.btnCatatanSend.visibility = View.GONE
            binding.btnAkhiriSesi.visibility = View.GONE
            binding.tieGejala.isEnabled = false
            binding.tieDiagnosis.isEnabled = false
            binding.tieCatatan.isEnabled = false
        }
    }

    companion object {
        const val TAG = "CatatanBottomSheetDialog"
        const val ID_CATATAN = "catatan"
        const val GEJALA = "gejala"
        const val DIAGNOSIS = "diagnosis"
        const val CATATAN = "catatan"
        const val STATUS = "status"
    }
}