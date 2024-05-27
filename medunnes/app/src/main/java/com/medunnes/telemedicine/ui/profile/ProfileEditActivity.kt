package com.medunnes.telemedicine.ui.profile

import android.app.DatePickerDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.Dokter
import com.medunnes.telemedicine.data.model.User
import com.medunnes.telemedicine.databinding.ActivityProfileEditBinding
import com.medunnes.telemedicine.utils.getImageUri
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfileEditActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfileEditBinding

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var datePicked: String = "date"
    private var currentImageUri: Uri? = null
    private var imagePath: String = "path"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnBack.setOnClickListener(this@ProfileEditActivity)
            btnEditSend.setOnClickListener(this@ProfileEditActivity)
            ivEditPicture.setOnClickListener(this@ProfileEditActivity)
            tilEditTglLahir.setEndIconOnClickListener { showDatePicker() }
        }

        lifecycleScope.launch {
            if (viewModel.getUserRole() == 1) {
                getDokterProfileData()
            } else {
                getUserProfileData()
            }

        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private suspend fun getDokterProfileData() {
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

    private suspend fun getUserProfileData() {
        viewModel.getUserProfile(viewModel.getUserLoginId()).observe(this@ProfileEditActivity) { data ->
            data.forEach {
                with(binding) {
                    tieEditNamaLengkap.setText(it.fullname)
                    tieEditTglLahir.setText(it.tanggalLahir)
                    tieEditNoTelepon.setText(it.noTelepon)
                    tieEditAlamat.setText(it.alamat)
                    tieEditEmail.setText(it.email)
                    tvEditRumahSakitTitle.visibility = View.GONE
                    tilEditRumahSakit.visibility = View.GONE

                    val path = Environment.getExternalStorageDirectory()
                    val imageFile = "${File(path, "/Android/data/com.medunnes.telemedicine${it.image}")}"
                    Glide.with(this@ProfileEditActivity)
                        .load(imageFile)
                        .into(ivEditPicture)
                        .clearOnDetach()
                }
            }
        }
    }

    private suspend fun updateDokterProfile() {
        val userId = viewModel.getUserLoginId()
        with(binding) {
            viewModel.getUserAndDokter(userId).observe(this@ProfileEditActivity) { data ->
                data.forEach {
                    val user = User(
                        it.user.id,
                        "${tieEditEmail.text}",
                        it.user.password,
                        "${tieEditNamaLengkap.text}",
                        datePicked,
                        it.user.jenisKelamin,
                        "${tieEditAlamat.text}",
                        "${tieEditNoTelepon.text}",
                        it.user.role,
                        imagePath
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
                                it.dokter.pendidikan,
                                it.dokter.spesialis,
                                userId
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun updateUserProfile() {
        val userId = viewModel.getUserLoginId()
        with(binding) {
            viewModel.getUserProfile(userId).observe(this@ProfileEditActivity) { data ->
                data.forEach {
                    val user = User(
                        it.id,
                        "${tieEditEmail.text}",
                        it.password,
                        "${tieEditNamaLengkap.text}",
                        datePicked,
                        it.jenisKelamin,
                        "${tieEditAlamat.text}",
                        "${tieEditNoTelepon.text}",
                        it.role,
                        imagePath
                    )
                    with(viewModel) {
                        updateUserProfile(user)
                    }
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val cYear = calendar.get(Calendar.YEAR)
        val cMonth = calendar.get(Calendar.MONTH)
        val cDay = calendar.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(
            this, { _, year, month, day ->
                calendar.set(year, month, day)
                val fullDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                binding.tieEditTglLahir.setText(fullDateFormat.format(calendar.time))
                datePicked = "${calendar.time}"

            }, cYear, cMonth, cDay)

        datePickerDialog.show()
    }

    private fun galeryStart() {
        galeryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val galeryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            makeToast("Tidak ada gambar yang tersedia")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivEditPicture.setImageURI(it)
        }
    }



    override fun onClick(view: View) {
        with(binding) {
            when(view) {
                btnBack -> finish()
                btnEditSend -> {
                    lifecycleScope.launch {
                        if (viewModel.getUserRole() == 1) {
                            updateDokterProfile()
                        } else {
                            updateUserProfile()
                        }
                    }
                    currentImageUri?.let { sourceUri ->
                        val uri = getImageUri(this@ProfileEditActivity, sourceUri)
                        try {
                            uri.let {
                                    try {
                                        val inputStream: InputStream? = this@ProfileEditActivity.contentResolver.openInputStream(sourceUri)
                                        val outputStream: OutputStream? = this@ProfileEditActivity.contentResolver.openOutputStream(it)

                                        inputStream?.use { input ->
                                            outputStream?.use { output ->
                                                val buffer = ByteArray(1024)
                                                var bytesRead: Int
                                                while (input.read(buffer).also { bytesRead = it } != -1) {
                                                    output.write(buffer, 0, bytesRead)
                                                }
                                            }
                                        }
                                        imagePath = "${it.path}"

                                } catch (e: Exception) {
                                    this@ProfileEditActivity.contentResolver.delete(uri, null, null)
                                }
                            }
                        } catch (e: Exception) {
                            Log.d("ERROR", e.toString())
                        }

                    }

                    lifecycleScope.launch {
                        if (viewModel.getUserRole() == 1) {
                            updateDokterProfile()
                        } else {
                            updateUserProfile()
                        }
                    }

                    makeToast("Data berhasil diperbarui")
                }
                ivEditPicture -> galeryStart()
            }
        }
    }
}