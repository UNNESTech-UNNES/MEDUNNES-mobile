package com.medunnes.telemedicine.ui.dosen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.databinding.ActivityLayananDosenBinding
import com.medunnes.telemedicine.ui.dosen.daftarMahasiswa.DaftarMahasiswaFragment
import com.medunnes.telemedicine.ui.dosen.konsultasi.KonsultasiDosenFragment

class LayananDosenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLayananDosenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayananDosenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val daftarMahasiswaFragment = DaftarMahasiswaFragment()
        val konsultasiDosenFragment = KonsultasiDosenFragment()
        val fragmentJanji = fragmentManager.findFragmentByTag(DaftarMahasiswaFragment::class.java.simpleName)
        val fragmentKonsultasi = fragmentManager.findFragmentByTag(KonsultasiDosenFragment::class.java.simpleName)

        if (intent.getStringExtra(FRAGMENT) == "0") {
            if (fragmentKonsultasi !is KonsultasiDosenFragment) {
                fragmentManager
                    .beginTransaction()
                    .add(R.id.dokter_frame_container, konsultasiDosenFragment, KonsultasiDosenFragment::class.java.simpleName)
                    .commit()
            }
            binding.tvAppBarTitle.text = getString(R.string.konsultasi)
        } else {
            if (fragmentJanji !is DaftarMahasiswaFragment) {
                fragmentManager
                    .beginTransaction()
                    .add(R.id.dokter_frame_container, daftarMahasiswaFragment, DaftarMahasiswaFragment::class.java.simpleName)
                    .commit()
            }
            binding.tvAppBarTitle.text = getString(R.string.daftar_mahasiswa)
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    companion object {
        const val FRAGMENT = "fragment"
    }
}