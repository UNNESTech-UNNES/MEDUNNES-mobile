package com.medunnes.telemedicine.ui.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val aboutFragment = AboutApplicationFragment()
        val faqFragment = FaqFragment()
        val fragmentAbout = fragmentManager.findFragmentByTag(AboutApplicationFragment::class.java.simpleName)
        val fragmentFaq = fragmentManager.findFragmentByTag(FaqFragment::class.java.simpleName)

        if (intent.getIntExtra(FRAGMENT, 0) == 1) {
            if (fragmentAbout !is AboutApplicationFragment) {
                fragmentManager
                    .beginTransaction()
                    .add(R.id.about_frame_container, aboutFragment, AboutApplicationFragment::class.java.simpleName)
                    .commit()
            }
        } else {
            if (fragmentFaq !is FaqFragment) {
                fragmentManager
                    .beginTransaction()
                    .add(R.id.about_frame_container, faqFragment, FaqFragment::class.java.simpleName)
                    .commit()
            }
        }

        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    companion object {
        const val FRAGMENT = "fragment"
    }
}