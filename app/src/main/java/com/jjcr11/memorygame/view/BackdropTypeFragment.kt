package com.jjcr11.memorygame.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentBackdropTypeBinding

class BackdropTypeFragment : Fragment() {

    private lateinit var binding: FragmentBackdropTypeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBackdropTypeBinding.inflate(inflater, container, false)

        val sharedPreferences = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)

        when (sharedPreferences?.getInt("type", 0)) {
            0 -> DrawableCompat.setTint(
                binding.ivSelectColors.drawable,
                ContextCompat.getColor(requireContext(), R.color.black)
            )
            1 -> DrawableCompat.setTint(
                binding.ivSelectNumbers.drawable,
                ContextCompat.getColor(requireContext(), R.color.black)
            )
            2 -> DrawableCompat.setTint(
                binding.ivSelectBoth.drawable,
                ContextCompat.getColor(requireContext(), R.color.black)
            )
        }

        binding.mcvColors.setOnClickListener {
            sharedPreferences?.edit()?.putInt("type", 0)?.apply()
            //(parentFragment as SettingsFragment).removeBackdrop()
        }

        binding.mcvNumbers.setOnClickListener {
            sharedPreferences?.edit()?.putInt("type", 1)?.apply()
            //(parentFragment as SettingsFragment).removeBackdrop()
        }

        binding.mcvBoth.setOnClickListener {
            sharedPreferences?.edit()?.putInt("type", 2)?.apply()
            //(parentFragment as SettingsFragment).removeBackdrop()
        }

        return binding.root
    }
}