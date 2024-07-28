package com.medunnes.telemedicine.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.medunnes.telemedicine.databinding.FragmentAboutApplicationBinding


class AboutApplicationFragment : Fragment() {
    private var _binding: FragmentAboutApplicationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAboutApplicationBinding.inflate(inflater, container, false)
        return binding.root
    }
}