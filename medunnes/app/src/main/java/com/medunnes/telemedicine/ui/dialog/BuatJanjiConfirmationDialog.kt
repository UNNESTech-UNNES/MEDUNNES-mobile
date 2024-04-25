package com.medunnes.telemedicine.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.DialogBuatJanjiAlertBinding
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel

class BuatJanjiConfirmationDialog : DialogFragment(), View.OnClickListener {
    private lateinit var binding: DialogBuatJanjiAlertBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogBuatJanjiAlertBinding.inflate(inflater, container, false)

        with(binding) {
            btnYes.setOnClickListener(this@BuatJanjiConfirmationDialog)
            btnNo.setOnClickListener(this@BuatJanjiConfirmationDialog)
        }
        return binding.root
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(isConfirm: Boolean)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    companion object {
        const val TAG = "BuatJanjiConfirmationDialog"
    }

    override fun onClick(view: View) {
        with(binding) {
            when(view) {
                btnYes -> onItemClickCallback.onItemClicked(true)
                btnNo -> onItemClickCallback.onItemClicked(false)
            }
        }
    }
}