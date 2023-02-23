package com.jjcr11.memorygame.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jjcr11.memorygame.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.clTheme.setOnClickListener {}
        binding.tvExportData.setOnClickListener {}
        binding.clType.setOnClickListener {}

        binding.clButton1.setOnClickListener {}
        binding.clButton2.setOnClickListener {}
        binding.clButton3.setOnClickListener {}
        binding.clButton4.setOnClickListener {}
        binding.clButton5.setOnClickListener {}
        binding.clButton6.setOnClickListener {}
        binding.clButton7.setOnClickListener {}
        binding.clButton8.setOnClickListener {}

        return binding.root
    }
}