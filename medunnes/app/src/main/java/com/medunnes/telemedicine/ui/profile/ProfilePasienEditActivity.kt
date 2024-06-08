package com.medunnes.telemedicine.ui.profile

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
import com.medunnes.telemedicine.databinding.ActivityProfilePasienEditBinding
import com.medunnes.telemedicine.utils.getImageUri
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfilePasienEditActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfilePasienEditBinding

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var datePicked: String = "date"
    private var currentImageUri: Uri? = null
    private var imagePath: String? = null
    private val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
    private val fullDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    private lateinit var date: Date
    private var pasienId: Int = 0
    private var kelamin: String = "kelamin"
    private var status: String = "status"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePasienEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnBack.setOnClickListener(this@ProfilePasienEditActivity)
            btnEditSend.setOnClickListener(this@ProfilePasienEditActivity)
            ivEditPicture.setOnClickListener(this@ProfilePasienEditActivity)
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
        setPasienProfileData()
    }

    private suspend fun setPasienProfileData() {
        val userId = viewModel.getUserLoginId()
        viewModel.getPasienByUserLogin(userId)
        viewModel.pasien.observe(this) { data ->
            if (!data.isNullOrEmpty()) {
                data.forEach {
                    with(binding) {
                        pasienId = it.idPasien
                        tieEditNoTelepon.setText(it.noTlp)
                        tieEditNik.setText(it.nik.toString())
                        tieEditAlamat.setText(it.alamat)
                        tieEditTb.setText(it.tB.toString())
                        tieEditBb.setText(it.bB.toString())
                        imagePath =  it.imgPasien
                        kelamin = it.jenisKelamin
                        status = it.status
                    }
                }
            }
        }
    }

    private suspend fun updateUserProfile() {
        val userId = viewModel.getUserLoginId()
        try {
            viewModel.updatePasien(
                userId,
                userId.toLong(),
                binding.tieEditNik.text.toString().toLong(),
                "${binding.tieEditNamaLengkap.text}",
                imagePath,
                kelamin,
                "${binding.tieEditAlamat.text}",
                "${binding.tieEditNoTelepon.text}",
                binding.tieEditTb.text.toString().toInt(),
                binding.tieEditBb.text.toString().toInt(),
                status
            )

            makeToast("Data berhasil diperbarui")
        } catch (e: Exception) {
            makeToast("Data gagal diperbarui")
            Log.d("EXCEPTION", e.message.toString())
        }
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
            val uri = getImageUri(this@ProfilePasienEditActivity)
            try {
                uri.let {
                    try {
                        val inputStream: InputStream? = this@ProfilePasienEditActivity.contentResolver.openInputStream(sourceUri)
                        val outputStream: OutputStream? = this@ProfilePasienEditActivity.contentResolver.openOutputStream(it)

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
                        this@ProfilePasienEditActivity.contentResolver.delete(uri, null, null)
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