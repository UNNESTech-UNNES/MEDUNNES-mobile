package com.medunnes.telemedicine.ui.profile

import android.app.DatePickerDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.ActivityProfileEditDokterBinding
import com.medunnes.telemedicine.utils.getImageUri
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfileEditDokterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfileEditDokterBinding

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null
    private var imagePath: String? = null
    private var dokterId: Long = 0
    private var spesialisId: Long = 0
    private var kelamin: String = "kelamin"
    private var status: String = "status"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditDokterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnBack.setOnClickListener(this@ProfileEditDokterActivity)
            btnEditSend.setOnClickListener(this@ProfileEditDokterActivity)
            ivEditPicture.setOnClickListener(this@ProfileEditDokterActivity)
            tilEditTglMulaiAktif.setEndIconOnClickListener { showDatePicker() }
        }

        lifecycleScope.launch { getUserProfileData() }
    }

    private fun makeToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private suspend fun getUserProfileData() {
        val userId = viewModel.getUserLoginId()
        viewModel.getUserLogin(userId)
        viewModel.user.observe(this) { data ->
            if (!data.isNullOrEmpty()) {
                data.forEach {
                    binding.tieEditNamaLengkap.setText(it.name)
                }
            }
        }
        setDokterProfileData()
    }

    private suspend fun setDokterProfileData() {
        val userId = viewModel.getUserLoginId()

        viewModel.getDokterByUserLogin(userId)
        viewModel.dokter.observe(this) { data ->
            if (!data.isNullOrEmpty()) {
                data.forEach {
                    with(binding) {
                        dokterId = it.idDokter
                        tieEditNamaLengkap.setText(it.namaDokter)
                        tieEditTitleDepan.setText(it.titleDepan)
                        tieEditTitleBelakang.setText(it.titleBelakang)
                        tieEditNoReg.setText(it.noReg.toString())
                        tieEditAlumniKampus.setText(it.alumni)
                        tieEditNoTelepon.setText(it.noTlp)
                        tieEditAlamat.setText(it.alamat)
                        tieEditTempatPraktik.setText(it.tempatKerja)
                        tieEditTahunLulus.setText(it.tahunLulus.toString())
                        tieEditTglMulaiAktif.setText(it.tglMulaiAktif)
                        kelamin = it.jenisKelamin
                        status = it.status
                        spesialisId = it.spesialisId
                        imagePath = it.imgDokter
                    }
                }
            } else {
                Log.d("DATA", data.toString())
            }
        }
    }

    private suspend fun updateUserProfile() {
        val userId = viewModel.getUserLoginId()
        try {
            viewModel.updateDokter(
                userId.toLong(),
                userId.toLong(),
                spesialisId,
                "${binding.tieEditTitleDepan.text}",
                "${binding.tieEditNamaLengkap.text}",
                "${binding.tieEditTitleBelakang.text}",
                imagePath,
                "${binding.tieEditAlamat.text}",
                "${binding.tieEditNoTelepon.text}",
                "${binding.tieEditTempatPraktik.text}",
                binding.tieEditTahunLulus.text.toString().toInt(),
                "${binding.tieEditTglMulaiAktif.text}",
                "${binding.tieEditAlumniKampus.text}",
                binding.tieEditNoReg.text.toString().toLong(),
                kelamin,
                status
            )

            makeToast("Data berhasil diperbarui")
        } catch (e: Exception) {
            makeToast("Data gagal diperbarui")
            Log.d("EXCEPTION", e.message.toString())
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
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateFormatted =  dateFormat.format(calendar.time)
                binding.tieEditTglMulaiAktif.setText(dateFormatted)
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

    private fun uploadImage() {
        currentImageUri?.let { sourceUri ->
            val uri = getImageUri(this@ProfileEditDokterActivity)
            try {
                uri.let { it ->
                    try {
                        val inputStream: InputStream? = this@ProfileEditDokterActivity.contentResolver.openInputStream(sourceUri)
                        val outputStream: OutputStream? = this@ProfileEditDokterActivity.contentResolver.openOutputStream(it)

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
                        this@ProfileEditDokterActivity.contentResolver.delete(uri, null, null)
                    }
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }

        }
    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                btnBack -> finish()
                btnEditSend -> {
                    lifecycleScope.launch {
                        uploadImage()
                        updateUserProfile()
                    }
                }
                ivEditPicture -> galeryStart()
                else -> makeToast("NO CLICK")
            }
        }
    }
}