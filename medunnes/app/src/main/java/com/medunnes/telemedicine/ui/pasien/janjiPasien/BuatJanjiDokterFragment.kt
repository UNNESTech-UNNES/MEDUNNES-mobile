package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.response.SesiDataItem
import com.medunnes.telemedicine.databinding.FragmentBuatJanjiDokterBinding
import com.medunnes.telemedicine.ui.adapter.SesiAdapter
import com.medunnes.telemedicine.ui.dialog.BuatJanjiConfirmationDialog
import com.medunnes.telemedicine.ui.dialog.BuatJanjiSuccessDialog
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import com.medunnes.telemedicine.utils.imageBaseUrl
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BuatJanjiDokterFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentBuatJanjiDokterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var datePicked: String = "date"
    private var sesiPicked: String? = null
    private var listSesi = ArrayList<SesiDataItem>()
    private val bjcd = BuatJanjiConfirmationDialog()
    private val bjsd = BuatJanjiSuccessDialog()
    private var sesiNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBuatJanjiDokterBinding.inflate(inflater, container, false)
        setDoctorProfile()

        lifecycleScope.launch { setPasienData() }

        with(binding) {
            tilJanjiTanggal.setEndIconOnClickListener { showDatePicker() }
            btnBuatJanjiDokter.setOnClickListener(this@BuatJanjiDokterFragment)
            tvFormIsEmpty.visibility = View.VISIBLE
            tvSesiPicked.visibility = View.GONE
            tvDatePicked.visibility = View.GONE
            tilPasien.setEndIconOnClickListener { selectPatient() }
        }

        getSesiList()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setDoctorProfile() {
        val doctorId = arguments?.getInt(DOCTOR_ID)
        doctorId?.let { viewModel.getDokterById(it) }
        val spesialis = resources.getStringArray(R.array.spesialissasi)
        viewModel.dokter.observe(viewLifecycleOwner) { data ->
            with(binding) {
                data.forEach { dokter ->
                    tvDoctorName.text = getString(R.string.nama_and_titel, dokter.titleDepan, dokter.namaDokter, dokter.titleBelakang)
                    tvDoctorSpeciality.text = spesialis[(dokter.spesialisId.toInt())-1]
                    tvDoctorExperience.text = dokter.tempatKerja

                    val imagePath = "${imageBaseUrl()}/${dokter.imgDokter}"
                    Glide.with(this@BuatJanjiDokterFragment)
                        .load(imagePath)
                        .into(ivDoctorImage)
                        .clearOnDetach()
                }
            }
        }
    }

    private suspend fun setPasienData() {
        var pasienId = arguments?.getInt(PASIEN_ID)
        val pasienTambahanId = arguments?.getInt(PASIEN_TAMBAHAN_ID)
        if (pasienId != null && pasienTambahanId != null && pasienId != 0 && pasienTambahanId != 0) {
            viewModel.getPasienTambahanById(pasienId, pasienTambahanId)
            viewModel.pasienTambahan.observe(viewLifecycleOwner) { data ->
                if (!data.isNullOrEmpty()) {
                    data.forEach {
                        binding.tiePasienPicked.setText(it.namaPasienTambahan)
                        binding.tiePasienIdPicked.setText(it.pasienId.toString())
                        binding.tiePasienTambahanIdPicked.setText(it.idPasienTambahan.toString())
                    }
                }
            }
        } else {
            viewModel.getPasienByUserLogin(viewModel.getUserLoginId())
            viewModel.pasien.observe(viewLifecycleOwner) { pasien ->
                pasien.forEach { pasienId = it.idPasien.toInt() }
                viewModel.getPasienTambahanByPasien(pasienId!!)
                viewModel.pasienTambahan.observe(viewLifecycleOwner) { pasienTambahan ->
                    pasienTambahan.forEach { pasienId = it.idPasienTambahan }
                    binding.tiePasienPicked.setText(pasienTambahan[0].namaPasienTambahan)
                    binding.tiePasienIdPicked.setText(pasienTambahan[0].pasienId.toString())
                    binding.tiePasienTambahanIdPicked.setText(pasienTambahan[0].idPasienTambahan.toString())

                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance(Locale("id", "ID"))
        val cYear = calendar.get(Calendar.YEAR)
        val cMonth = calendar.get(Calendar.MONTH)
        val cDay = calendar.get(Calendar.DATE)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, month, day ->
                calendar.set(year, month, day)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val fullDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                val date = dateFormat.format(calendar.time)
                binding.tieSesiDate.setText(date)
                binding.tvDatePicked.text = fullDateFormat.format(calendar.time)
                datePicked = date

                binding.tvFormIsEmpty.visibility = View.GONE
                binding.tvDatePicked.visibility = View.VISIBLE

            }, cYear, cMonth, cDay)

        datePickerDialog.show()
    }

    private fun showRecycleList() {
        val orientation = resources.configuration.orientation
        val column: Int = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            2
        } else {
            4
        }
        binding.rvSesiList.layoutManager = GridLayoutManager(context, column)
        val sesiAdapter = SesiAdapter(listSesi)
        binding.rvSesiList.adapter = sesiAdapter

        sesiAdapter.setOnItemClickCallback(object : SesiAdapter.OnItemClickCallback {
            override fun onClick(sesi: SesiDataItem) {
                with(binding) {
                    tvFormIsEmpty.visibility = View.GONE
                    tvSesiPicked.visibility = View.VISIBLE
                    sesiPicked = getString(R.string.no_sesi, sesi.idSesi)
                    tvSesiPicked.text = sesiPicked
                    sesiNumber = sesi.idSesi
                }
            }
        })
    }

    private fun getSesiList() : ArrayList<SesiDataItem> {
        viewModel.getAllSesi()
        viewModel.sesi.observe(viewLifecycleOwner) { sesi ->
            listSesi.clear()
            listSesi.addAll(sesi)
            showRecycleList()
        }
        return listSesi
    }

    private fun postJanji(isConfirm: Boolean) {
        val doctorId = arguments?.getInt(DOCTOR_ID)
        if (isConfirm) {
            bjcd.dismiss()
            try {
                val pasienIdPicked = binding.tiePasienIdPicked.text.toString().toLong()
                val catatan ="${binding.tieSesiCatatan.text}"
                lifecycleScope.launch {
                    viewModel.insertJanji(
                        pasienIdPicked,
                        doctorId!!.toLong(),
                        binding.tiePasienTambahanIdPicked.text.toString().toLong(),
                        sesiNumber.toLong(),
                        datePicked,
                        catatan,
                        "pending"
                    )
                }
                showSuccessDialog()
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        } else {
            bjcd.dismiss()
        }
    }

    private fun showConfirmationDialog() {
        val bundle = Bundle()
        bundle.putString(BuatJanjiConfirmationDialog.DIALOG, getString(R.string.alert_buat_janji_deskripsi))
        bjcd.arguments = bundle
        bjcd.show(childFragmentManager, BuatJanjiConfirmationDialog.TAG)

        bjcd.setOnItemClickCallback(object : BuatJanjiConfirmationDialog.OnItemClickCallback {
            override fun onItemClicked(isConfirm: Boolean) {
                postJanji(isConfirm)
            }

        })
    }

    private fun selectPatient() {
        val doctorId = arguments?.getInt(DOCTOR_ID)
        val daftarPasienFragment = DaftarPasienFragment()
        val fragmentManager = parentFragmentManager
        val bundle = Bundle()
        doctorId?.let { bundle.putInt(DaftarPasienFragment.DOKTER_ID, it) }
        daftarPasienFragment.arguments = bundle

        fragmentManager.popBackStack()
        fragmentManager.beginTransaction()
            .replace(R.id.pasien_frame_container, daftarPasienFragment, DaftarPasienFragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()

    }

    private fun showSuccessDialog() {
        val bundle = Bundle()
        bundle.putString(BuatJanjiConfirmationDialog.DIALOG, getString(R.string.buat_janji_sukses))
        bjcd.arguments = bundle
        bjsd.show(childFragmentManager, BuatJanjiSuccessDialog.TAG)
    }

    companion object {
        const val DOCTOR_ID = "doctor_id"
        const val PASIEN_ID = "pasien_id"
        const val PASIEN_TAMBAHAN_ID = "pasien_tambahan_id"
        const val PASIEN_NAME = "pasien_nama"
    }

    override fun onClick(view: View?) {
        with(binding) {
            when(view) {
                tilJanjiTanggal -> showDatePicker()
                btnBuatJanjiDokter -> {
                    if (datePicked.isNotEmpty()
                        && !sesiPicked.isNullOrEmpty()
                        && !tiePasienIdPicked.text.isNullOrEmpty()
                        && !tiePasienTambahanIdPicked.text.isNullOrEmpty()
                        && !tieSesiCatatan.text.isNullOrEmpty()
                    ) {
                        showConfirmationDialog()
                    } else {
                        Toast.makeText(context, "Harap lengkapi data", Toast.LENGTH_SHORT).show()
                    }

                }
                tilPasien -> selectPatient()
            }
        }
    }
}