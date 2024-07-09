package com.medunnes.telemedicine.ui.pasien

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.databinding.ActivityLayananPasienBinding
import com.medunnes.telemedicine.ui.pasien.janjiPasien.JanjiPasienFragment
import com.medunnes.telemedicine.ui.pasien.konsultasiPasien.KonsultasiPasienFragment

class LayananPasienActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLayananPasienBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayananPasienBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val janjiPasienFragment = JanjiPasienFragment()
        val konsultasiPasienFragment = KonsultasiPasienFragment()
        val fragmentJanjiPasien = fragmentManager.findFragmentByTag(JanjiPasienFragment::class.java.simpleName)
        val fragmentKonsultasiPasien = fragmentManager.findFragmentByTag(KonsultasiPasienFragment::class.java.simpleName)

        if (intent.getStringExtra(FRAGMENT) == "1") {
            if (fragmentJanjiPasien !is JanjiPasienFragment) {
                fragmentManager
                    .beginTransaction()
                    .add(R.id.pasien_frame_container, janjiPasienFragment, JanjiPasienFragment::class.java.simpleName)
                    .commit()
            }
        } else {
            if (fragmentKonsultasiPasien !is KonsultasiPasienFragment) {
                fragmentManager
                    .beginTransaction()
                    .add(R.id.pasien_frame_container, konsultasiPasienFragment, KonsultasiPasienFragment::class.java.simpleName)
                    .commit()
            }
        }

        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    companion object {
        const val FRAGMENT = "fragment"
    }
}