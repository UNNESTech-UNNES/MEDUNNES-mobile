package com.medunnes.telemedicine.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.medunnes.telemedicine.databinding.DialogBuatJanjiSuccessBinding

class BuatJanjiSuccessDialog : DialogFragment(), View.OnClickListener {
    private lateinit var binding: DialogBuatJanjiSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBuatJanjiSuccessBinding.inflate(inflater, container, false)
        binding.btnOk.setOnClickListener(this)
        return binding.root
    }

    companion object {
        const val TAG = "BuatJanjiSuccessDialog"
    }

    override fun onClick(view: View) {
        when(view) {
            binding.btnOk -> dismiss()
        }
    }
}