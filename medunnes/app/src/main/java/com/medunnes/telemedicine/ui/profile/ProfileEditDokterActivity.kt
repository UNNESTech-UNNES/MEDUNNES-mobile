package com.medunnes.telemedicine.ui.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.databinding.ActivityProfileEditDokterBinding
import com.medunnes.telemedicine.utils.imageBaseUrl
import com.medunnes.telemedicine.utils.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

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
        showProgressBar(true)
        val userId = viewModel.getUserLoginId()
        viewModel.getDokterByUserLogin(userId)
        viewModel.dokter.observe(this) { data ->
            if (!data.isNullOrEmpty()) {
                showProgressBar(false)
                data.forEach {
                    with(binding) {
                        dokterId = it.idDokter
                        tieEditNoReg.setText(it.nim.toString())
                        tieEditNoTelepon.setText(it.noTlp)
                        tieEditAlamat.setText(it.alamat)
                        kelamin = it.jenisKelamin
                        status = it.status
                        spesialisId = it.spesialisId
                        imagePath = it.imgDokter

                        if (!it.imgDokter.isNullOrEmpty()) {
                            val imagePath = "${imageBaseUrl()}/${it.imgDokter}"
                            Glide.with(this@ProfileEditDokterActivity)
                                .load(imagePath)
                                .into(binding.ivEditPicture)
                                .clearOnDetach()
                        }
                    }
                }
            }
        }
    }

    private suspend fun updateUserProfile() {
        val userId = viewModel.getUserLoginId()
        try {
            viewModel.updateUser(
                userId,
                "${binding.tieEditNamaLengkap.text}"
            )

            viewModel.updateDokter(
                userId.toLong(),
                userId.toLong(),
                spesialisId,
                imagePath,
                "${binding.tieEditAlamat.text}",
                "${binding.tieEditNoTelepon.text}",
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
            val imageFile = uriToFile(sourceUri, this)
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            lifecycleScope.launch {
                try {
                    val uid = viewModel.getUserLoginId()
                    viewModel.uploadImageDokter(uid, multipartBody)
                } catch (e: Exception) {
                    Log.d("UPLOAD IMAGE FAIL", e.message.toString())
                }
            }
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.cvProgressBar.visibility = View.VISIBLE
            binding.btnEditSend.isClickable = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.cvProgressBar.visibility = View.GONE
            binding.btnEditSend.isClickable = true
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