package com.jjcr11.memorygame.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentBackdropBinding

class BackdropFragment : Fragment() {

    private lateinit var binding: FragmentBackdropBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBackdropBinding.inflate(inflater, container, false)

        val sharedPreferences = activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val scoreSelected = sharedPreferences?.getBoolean("scoreSelected", true)

        val colors =
            if (scoreSelected!!)
                Pair(R.color.black, R.color.transparent)
            else
                Pair(R.color.transparent, R.color.black)

        DrawableCompat.setTint(
            binding.ivSelectScore.drawable,
            ContextCompat.getColor(requireContext(), colors.first)
        )
        DrawableCompat.setTint(
            binding.ivSelectDate.drawable,
            ContextCompat.getColor(requireContext(), colors.second)
        )

        binding.mcvScore.setOnClickListener {
            sharedPreferences.edit().putBoolean("scoreSelected", true).apply()
            (parentFragment as ScoreFragment).removeBackdrop()
        }

        binding.mcvDate.setOnClickListener {
            sharedPreferences.edit().putBoolean("scoreSelected", false).apply()
            (parentFragment as ScoreFragment).removeBackdrop()
        }

        return binding.root
    }
}