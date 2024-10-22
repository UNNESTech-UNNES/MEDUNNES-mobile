package com.medunnes.telemedicine.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.databinding.DialogDetailPasienBinding
import com.medunnes.telemedicine.utils.imageBaseUrl
import java.text.SimpleDateFormat
import java.util.Locale

class PasienDetailDialog : DialogFragment(), View.OnClickListener {
    private lateinit var binding: DialogDetailPasienBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDetailPasienBinding.inflate(inflater, container, false)
        setPasienProfile()

        with(binding) {
            btnSetuju.setOnClickListener(this@PasienDetailDialog)
            btnTidakSetuju.setOnClickListener(this@PasienDetailDialog)
        }
        return binding.root
    }

    private fun setPasienProfile() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val fullDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val date = arguments?.getString(TANGGAL_PASIEN)?.let { dateFormat.parse(it) }

        with(binding) {
            tvPasienName.text = arguments?.getString(NAMA_PASIEN)
            tvPasienSesi.text = arguments?.getString(SESI_PASIEN)
            tvPasienTanggal.text = date?.let { fullDateFormat.format(it) }
            tvPasienCatatan.text = arguments?.getString(CATATAN)

            val imgPasien = arguments?.getString(IMG_PASIEN)
            if (!imgPasien.isNullOrEmpty()) {
                val imagePath = "${imageBaseUrl()}/$imgPasien"
                Glide.with(this@PasienDetailDialog)
                    .load(imagePath)
                    .into(ivPasienImage)
                    .clearOnDetach()
            }
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(isDisetujui: Boolean)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    companion object {
        const val NAMA_PASIEN = "nama_pasien"
        const val SESI_PASIEN = "sesi_pasien"
        const val TANGGAL_PASIEN = "tanggal_pasien"
        const val IMG_PASIEN = "img_pasien"
        const val CATATAN = "catatan"
        const val TAG = "PasienDetailDialog"
    }

    override fun onClick(view: View) {
        with(binding) {
            when(view) {
                btnSetuju -> onItemClickCallback.onItemClicked(true)
                btnTidakSetuju -> onItemClickCallback.onItemClicked(false)
            }
        }
    }
}