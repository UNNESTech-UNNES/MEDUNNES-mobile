package com.medunnes.telemedicine.ui.pasien.janjiPasien

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.ViewModelFactory
import com.medunnes.telemedicine.data.response.PasienTambahanDataItem
import com.medunnes.telemedicine.databinding.FragmentDaftarPasienBinding
import com.medunnes.telemedicine.ui.adapter.PasienListAdapter
import com.medunnes.telemedicine.ui.dialog.BuatJanjiSuccessDialog
import com.medunnes.telemedicine.ui.dialog.DeletePasienConfirmationDialog
import com.medunnes.telemedicine.ui.pasien.LayananPasienViewModel
import kotlinx.coroutines.launch

class DaftarPasienFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentDaftarPasienBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<LayananPasienViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val listPasien = ArrayList<PasienTambahanDataItem>()

    private var pasienId: Int = 0
    private var pasienTambahanId: Int = 0
    private var pasienName: String = ""
    private var doctorId: Int = 0

    private val dpcd = DeletePasienConfirmationDialog()
    private val bjsd = BuatJanjiSuccessDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDaftarPasienBinding.inflate(inflater, container, false)

        lifecycleScope.launch { getDataPasien() }

        doctorId = arguments?.getInt(DOKTER_ID)!!
        binding.btnTambahPasien.setOnClickListener(this)
        binding.btnSimpan.setOnClickListener(this)

        return binding.root
    }

    private suspend fun getDataPasien() {
        val uid = viewModel.getUserLoginId()
        var pasienId = 0
        // Get pasienId
        viewModel.getPasienByUserLogin(uid)
        viewModel.pasien.observe(viewLifecycleOwner) { data ->
            data.forEach { pasienId = it.idPasien.toInt() }

            // Get PasienTambahan
            viewModel.getPasienTambahanByPasien(pasienId)
            viewModel.pasienTambahan.observe(viewLifecycleOwner) { pasien ->
                if (!pasien.isNullOrEmpty()) {
                    listPasien.clear()
                    listPasien.addAll(pasien)
                    showRecyclerList(listPasien)
                }
            }
        }
    }

    private fun showRecyclerList(listAdapter: ArrayList<PasienTambahanDataItem>) {
        binding.rvPatientList.layoutManager = LinearLayoutManager(context)
        val pasienAdapter = PasienListAdapter(listAdapter)
        binding.rvPatientList.setItemViewCacheSize(listAdapter.size)
        binding.rvPatientList.adapter = pasienAdapter

        pasienAdapter.setOnItemClickCallback(object : PasienListAdapter.OnItemClickCallback {
            override fun onItemClicked(pasien: PasienTambahanDataItem) {

            }

            override fun onEditButtonClicked(pasien: PasienTambahanDataItem) {
                val editPasienFragment = EditPasienFragment()
                val fragmentManager = parentFragmentManager
                val bundle = Bundle()
                bundle.putInt(EditPasienFragment.PASIEN_ID, pasien.pasienId)
                editPasienFragment.arguments = bundle

                fragmentManager.beginTransaction()
                    .replace(R.id.pasien_frame_container, editPasienFragment, EditPasienFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }

            override fun onDeleteButtonClicked(pasien: PasienTambahanDataItem) {
                showConfirmationDialog(pasien)
            }

            override fun onRadioButtonChecked(pasien: PasienTambahanDataItem) {
                pasienId = pasien.pasienId
                pasienTambahanId = pasien.idPasienTambahan
                pasienName = pasien.namaPasienTambahan
            }

        })
    }

    private fun showConfirmationDialog(pasien: PasienTambahanDataItem) {
        val bundle = Bundle()
        bundle.putString(DeletePasienConfirmationDialog.DIALOG, getString(R.string.delete_pasien_alert_deskripsi))
        dpcd.arguments = bundle
        dpcd.show(childFragmentManager, DeletePasienConfirmationDialog.TAG)

        dpcd.setOnItemClickCallback(object : DeletePasienConfirmationDialog.OnItemClickCallback {
            override fun onItemClicked(isConfirm: Boolean) {
                deletePasien(pasien, isConfirm)
            }

        })
    }

    private fun showSuccessDialog() {
        val bundle = Bundle()
        bundle.putString(DeletePasienConfirmationDialog.DIALOG, getString(R.string.buat_janji_sukses))
        dpcd.arguments = bundle
        dpcd.dismiss()
        bjsd.show(childFragmentManager, BuatJanjiSuccessDialog.TAG)
    }

    private fun deletePasien(pasien: PasienTambahanDataItem, isConfirm: Boolean) {
        if (isConfirm) {
            try {
                //viewModel.deletePasien(pasien)
                showSuccessDialog()
            } catch (e: Exception) {
                Log.d("ERROR", e.toString())
            }
        } else {
            dpcd.dismiss()
        }
    }

    override fun onClick(view: View) {
        with(binding) {
            when(view) {
                btnTambahPasien -> {
                    val tambahPasienFragment = TambahPasienFragment()
                    val parentManager = parentFragmentManager
                    parentManager.beginTransaction()
                        .replace(R.id.pasien_frame_container, tambahPasienFragment, TambahPasienFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }

                btnSimpan -> {
                    val buatJanjiFragment = BuatJanjiDokterFragment()
                    val parentManager = parentFragmentManager
                    val bundle = Bundle()
                    bundle.putInt(BuatJanjiDokterFragment.PASIEN_ID, pasienId)
                    bundle.putInt(BuatJanjiDokterFragment.PASIEN_TAMBAHAN_ID, pasienTambahanId)
                    bundle.putString(BuatJanjiDokterFragment.PASIEN_NAME, pasienName)
                    bundle.putInt(BuatJanjiDokterFragment.DOCTOR_ID, doctorId)
                    buatJanjiFragment.arguments = bundle

                    parentManager.popBackStack()
                    parentManager.beginTransaction()
                        .replace(R.id.pasien_frame_container, buatJanjiFragment, BuatJanjiDokterFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }

                else -> { /*DO NOTHING */ }

            }
        }
    }

    companion object {
        const val DOKTER_ID = "dokter_id"
    }

}