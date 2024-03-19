package com.medunnes.telemedicine.ui.dokter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.databinding.ActivityLayananDokterBinding
import com.medunnes.telemedicine.ui.dokter.janji.JanjiDokterFragment
import com.medunnes.telemedicine.ui.dokter.konsultasi.KonsultasiFragment

class LayananDokterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLayananDokterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayananDokterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val janjiDokterFragment = JanjiDokterFragment()
        val konsultasiFragment = KonsultasiFragment()
        val fragmentJanji = fragmentManager.findFragmentByTag(JanjiDokterFragment::class.java.simpleName)
        val fragmentKonsultasi = fragmentManager.findFragmentByTag(KonsultasiFragment::class.java.simpleName)

        if (intent.getStringExtra(FRAGMENT) == "0") {
            if (fragmentKonsultasi !is KonsultasiFragment) {
                fragmentManager
                    .beginTransaction()
                    .add(R.id.dokter_frame_container, konsultasiFragment, KonsultasiFragment::class.java.simpleName)
                    .commit()
            }
            binding.tvAppBarTitle.text = getString(R.string.konsultasi)
        } else {
            if (fragmentJanji !is JanjiDokterFragment) {
                fragmentManager
                    .beginTransaction()
                    .add(R.id.dokter_frame_container, janjiDokterFragment, JanjiDokterFragment::class.java.simpleName)
                    .commit()
            }
            binding.tvAppBarTitle.text = getString(R.string.approve_janji_pasien)
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    companion object {
        const val FRAGMENT = "fragment"
    }
}