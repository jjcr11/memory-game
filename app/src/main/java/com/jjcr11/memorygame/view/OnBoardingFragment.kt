package com.jjcr11.memorygame.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)

        binding.mcvNext.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_onBoardingFragment_to_onBoardingFragment2)
        }

        return binding.root
    }
}