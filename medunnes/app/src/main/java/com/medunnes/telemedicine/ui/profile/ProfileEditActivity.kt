package com.medunnes.telemedicine.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.Dokter
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.databinding.ActivityProfileEditBinding
import kotlinx.coroutines.launch

class ProfileEditActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfileEditBinding

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnBack.setOnClickListener(this@ProfileEditActivity)
            btnEditSend.setOnClickListener(this@ProfileEditActivity)
            ivEditPicture.setOnClickListener(this@ProfileEditActivity)
        }

        lifecycleScope.launch { getUserProfileData() }
    }

    private fun makeToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private suspend fun getUserProfileData() {
        viewModel.getUserAndDokter(viewModel.getUserLoginId()).observe(this@ProfileEditActivity) { data ->
            data.forEach {
                with(binding) {
                    tieEditNamaLengkap.setText(it.user.fullname)
                    tieEditTglLahir.setText(it.user.tanggalLahir)
                    tieEditNoTelepon.setText(it.user.noTelepon)
                    tieEditRumahSakit.setText(it.dokter.tempatPraktik)
                    tieEditAlamat.setText(it.user.alamat)
                    tieEditEmail.setText(it.user.email)
                }
            }
        }
    }

    private suspend fun updateUserProfile() {
        val userId = viewModel.getUserLoginId()
        with(binding) {
            viewModel.getUserAndDokter(userId).observe(this@ProfileEditActivity) { data ->
                data.forEach {
                    val user = User(
                        it.user.id,
                        "${tieEditEmail.text}",
                        it.user.password,
                        "${tieEditNamaLengkap.text}",
                        "${tieEditTglLahir.text}",
                        it.user.jenisKelamin,
                        "${tieEditAlamat.text}",
                        "${tieEditNoTelepon.text}"
                    )
                    with(viewModel) {
                        updateUserProfile(user)
                        updateDokter(
                            Dokter(
                                it.dokter.userId,
                                it.dokter.titelDepan,
                                it.dokter.titelBelakang,
                                it.dokter.noStr,
                                "${tieEditRumahSakit.text}",
                                userId
                            )
                        )
                    }
                }
            }
        }
    }

    override fun onClick(view: View) {
        with(binding) {
            when(view) {
                btnBack -> finish()
                btnEditSend -> {
                    lifecycleScope.launch { updateUserProfile() }
                    makeToast("Data berhasil diperbarui")
                }
                ivEditPicture -> makeToast("Fitur belum tersedia")
            }
        }
    }
}