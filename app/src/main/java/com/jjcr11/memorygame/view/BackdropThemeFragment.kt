package com.jjcr11.memorygame.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentBackdropThemeBinding

class BackdropThemeFragment : Fragment() {

    private lateinit var binding: FragmentBackdropThemeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBackdropThemeBinding.inflate(inflater, container, false)

        val sharedPreferences = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)

        when (sharedPreferences?.getInt("theme", 2)) {
            0 -> DrawableCompat.setTint(
                binding.ivSelectLight.drawable,
                ContextCompat.getColor(requireContext(), R.color.black)
            )
            1 -> DrawableCompat.setTint(
                binding.ivSelectDark.drawable,
                ContextCompat.getColor(requireContext(), R.color.black)
            )
            2 -> DrawableCompat.setTint(
                binding.ivSelectSystem.drawable,
                ContextCompat.getColor(requireContext(), R.color.black)
            )
        }

        binding.mcvLight.setOnClickListener {
            sharedPreferences?.edit()?.putInt("theme", 0)?.apply()
            (parentFragment as SettingsFragment).removeBackdrop()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.mcvDark.setOnClickListener {
            sharedPreferences?.edit()?.putInt("theme", 1)?.apply()
            (parentFragment as SettingsFragment).removeBackdrop()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        binding.mcvSystem.setOnClickListener {
            sharedPreferences?.edit()?.putInt("theme", 2)?.apply()
            (parentFragment as SettingsFragment).removeBackdrop()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        return binding.root
    }
}