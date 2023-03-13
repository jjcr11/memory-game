package com.jjcr11.memorygame.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentOnBoarding4Binding

class OnBoardingFragment4 : Fragment() {

    private lateinit var binding: FragmentOnBoarding4Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoarding4Binding.inflate(inflater, container, false)

        val navController = findNavController()

        binding.mcvBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.mcvNext.setOnClickListener {
            navController.navigate(R.id.action_onBoardingFragment4_to_gameFragment)
            (activity as MainActivity).showBottomNavigation()

            val sharedPreferences =
                activity?.getSharedPreferences("settings", Context.MODE_PRIVATE)!!
            sharedPreferences.edit().putBoolean("onBoarding", false).apply()
        }

        return binding.root
    }

}