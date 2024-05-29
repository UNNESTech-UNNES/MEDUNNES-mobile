package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.model.Pasien
import com.medunnes.telemedicine.databinding.FragmentTambahPasienBinding
import com.medunnes.telemedicine.ui.dialog.BuatJanjiConfirmationDialog
import com.medunnes.telemedicine.ui.dialog.BuatJanjiSuccessDialog
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import com.medunnes.telemedicine.utils.getImageUri
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TambahPasienFragment : Fragment(),
    View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentTambahPasienBinding
    private lateinit var dataSpinner: String
    private lateinit var datePicked: String
    private val bjcd = BuatJanjiConfirmationDialog()
    private val bjsd = BuatJanjiSuccessDialog()
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var currentImageUri: Uri? = null
    private var imagePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTambahPasienBinding.inflate(layoutInflater)
        setSpinner()

        binding.btnSimpan.setOnClickListener(this)
        binding.cvPasienKartuIdentitas.setOnClickListener(this)

        return binding.root
    }

    private fun setSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.hubungan_pasien,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPasienHubungan.adapter = arrayAdapter
        }
        binding.spinnerPasienHubungan.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        dataSpinner = "${parent?.getItemAtPosition(pos)}"
        if (dataSpinner == "Diri sendiri") {
            lifecycleScope.launch {
                viewModel.getUserProfile(viewModel.getUserLoginId()).observe(viewLifecycleOwner) { data ->
                    data.forEach {
                        Log.d("DAATE", it.tanggalLahir.toString())
                        with(binding) {
                            tiePasienNama.setText(it.fullname)
                            tiePasienNama.isEnabled = false
                            tiePasienTglLahir.setText(it.tanggalLahir)
                            tilPasienTglLahir.setEndIconOnClickListener { /* DO NOTHING */ }
                        }
                    }
                }
            }
        } else {
            binding.tilPasienTglLahir.setEndIconOnClickListener { showDatePicker() }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // DO NOTHING
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val cYear = calendar.get(Calendar.YEAR)
        val cMonth = calendar.get(Calendar.MONTH)
        val cDay = calendar.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, month, day ->
                calendar.set(year, month, day)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.tiePasienTglLahir.setText(dateFormat.format(calendar.time))
                datePicked = "${calendar.time}"
            }, cYear, cMonth, cDay)

        datePickerDialog.show()
    }

    private suspend fun insertPasien(isConfirm: Boolean) {
        with(binding) {
            if (isConfirm) {
                try {
                    viewModel.insertPasien(Pasien(
                        0,
                        "${tiePasienNama.text}",
                        dataSpinner,
                        datePicked,
                        imagePath,
                        viewModel.getUserLoginId()
                    ))
                    uploadImage()
                    bjcd.dismiss()
                    showSuccessDialog()
                } catch (e: Exception) {
                    Log.d("ERROR", e.toString())
                }
            } else {
                bjcd.dismiss()
            }
        }
    }

    private fun showConfirmationDialog() {
        val bundle = Bundle()
        bundle.putString(BuatJanjiConfirmationDialog.DIALOG, "Pastikan data sudah benar")
        bjcd.arguments = bundle
        bjcd.show(childFragmentManager, BuatJanjiConfirmationDialog.TAG)

        bjcd.setOnItemClickCallback(object : BuatJanjiConfirmationDialog.OnItemClickCallback {
            override fun onItemClicked(isConfirm: Boolean) {
                lifecycleScope.launch { insertPasien(true) }
            }

        })
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
            Toast.makeText(context, "Tidak ada gambar yang tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivPasienKartuIdentitasEmpty.visibility = View.GONE
            binding.ivPasienKartuIdentitas.visibility = View.VISIBLE
            binding.ivPasienKartuIdentitas.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { sourceUri ->
            val uri = context?.let { getImageUri(it) }
            try {
                uri.let {
                    try {
                        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(sourceUri)
                        val outputStream: OutputStream? = it?.let { it1 ->
                            requireContext().contentResolver.openOutputStream(
                                it1
                            )
                        }

                        inputStream?.use { input ->
                            outputStream?.use { output ->
                                val buffer = ByteArray(1024)
                                var bytesRead: Int
                                while (input.read(buffer).also { bytesRead = it } != -1) {
                                    output.write(buffer, 0, bytesRead)
                                }
                            }
                        }
                        imagePath = "${it?.path}"

                    } catch (e: Exception) {
                        if (uri != null) {
                            requireContext().contentResolver.delete(uri, null, null)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }

        }
    }

    private fun showSuccessDialog() {
        val bundle = Bundle()
        bundle.putString(BuatJanjiConfirmationDialog.DIALOG, "Pasien berhasil ditambahkan")
        bjcd.arguments = bundle
        bjsd.show(childFragmentManager, BuatJanjiSuccessDialog.TAG)
    }

    override fun onClick(view: View) {
        when(view) {
            binding.btnSimpan -> showConfirmationDialog()
            binding.cvPasienKartuIdentitas -> {
                galeryStart()
            }
        }
    }

    companion object {
        const val INTENTION = "intention"
    }

}