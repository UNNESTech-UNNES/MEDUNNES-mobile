package com.medunnes.telemedicine.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.medunnes.telemedicine.databinding.DialogDeleteAlertBinding

class DeletePasienConfirmationDialog : DialogFragment(), View.OnClickListener {
    private lateinit var binding: DialogDeleteAlertBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteAlertBinding.inflate(inflater, container, false)
        binding.btnYes.setOnClickListener(this)
        binding.btnNo.setOnClickListener(this)

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
        const val TAG = "DeletePasienConfirmationDialog"
        const val DIALOG = "dialog"
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