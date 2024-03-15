package com.medunnes.telemedicine.ui.auth.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val registrasi1Fragment = Registrasi1Fragment()
        val fragment = fragmentManager.findFragmentByTag(Registrasi1Fragment::class.java.simpleName)

        if (fragment !is Registrasi1Fragment) {
            fragmentManager
                .beginTransaction()
                .add(R.id.frame_container, registrasi1Fragment, Registrasi1Fragment::class.java.simpleName)
                .commit()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}