package com.jjcr11.memorygame.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.FragmentOnBoarding2Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.hypot

class OnBoardingFragment2 : Fragment() {

    private lateinit var binding: FragmentOnBoarding2Binding
    private lateinit var mcvMain: MaterialCardView
    private lateinit var mcvMain2: MaterialCardView
    private val userColors = mutableListOf<String>()

    private lateinit var colorButton1: String
    private lateinit var colorButton2: String
    private lateinit var colorButton3: String
    private lateinit var colorButton4: String
    private lateinit var colorButton5: String
    private lateinit var colorButton6: String
    private lateinit var colorButton7: String
    private lateinit var colorButton8: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoarding2Binding.inflate(inflater, container, false)

        mcvMain = binding.mcvMain
        mcvMain2 = binding.mcvMain2
        setColorButtons()


        val navController = findNavController()

        binding.mcvBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.mcvNext.setOnClickListener {
            navController.navigate(R.id.action_onBoardingFragment2_to_onBoardingFragment3)
        }

        binding.mcvButton1.setOnClickListener { changeColor(colorButton1) }
        binding.mcvButton5.setOnClickListener { changeColor(colorButton5) }
        binding.mcvButton6.setOnClickListener { changeColor(colorButton6) }
        binding.mcvButton1.isEnabled = false
        binding.mcvButton5.isEnabled = false
        binding.mcvButton6.isEnabled = false

        Toast.makeText(requireContext(), "Memorize the sequence", Toast.LENGTH_SHORT).show()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.Main) {

            delay(800)
            var animation = setupAnimation(colorButton1)
            animation.start()
            mcvMain2.visibility = View.VISIBLE
            delay(800)
            animation = setupAnimation(colorButton5)
            animation.start()
            mcvMain2.visibility = View.VISIBLE
            delay(800)
            animation = setupAnimation(colorButton6)
            animation.start()
            mcvMain2.visibility = View.VISIBLE
            delay(800)
            mcvMain.visibility = View.INVISIBLE
            mcvMain2.visibility = View.INVISIBLE
            Toast.makeText(
                requireContext(),
                "Press the buttons on correct order",
                Toast.LENGTH_SHORT
            ).show()
            binding.mcvButton1.isEnabled = true
            binding.mcvButton5.isEnabled = true
            binding.mcvButton6.isEnabled = true
        }

    }

    private fun changeColor(color: String) {
        val animation = setupAnimation(color)
        animation.start()
        mcvMain2.visibility = View.VISIBLE
        userColors.add(color)
    }

    private fun setupAnimation(color: String): Animator {
        mcvMain.elevation = 1f
        mcvMain2.elevation = 200f
        mcvMain2.setBackgroundColor(Color.parseColor(color))

        val coords = Pair(mcvMain2.width / 2, mcvMain2.height / 2)
        val radius = hypot(coords.first.toDouble(), coords.second.toDouble()).toFloat()
        val animation = ViewAnimationUtils.createCircularReveal(
            mcvMain2,
            coords.first,
            coords.second,
            0f,
            radius
        ).let {
            it.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    val mcvAux = mcvMain
                    mcvMain = mcvMain2
                    mcvMain2 = mcvAux
                    mcvMain2.visibility = View.INVISIBLE

                    if (userColors.size == 3) {
                        if (userColors == listOf(colorButton1, colorButton5, colorButton6)) {
                            binding.mcvNext.visibility = View.VISIBLE
                        }
                        userColors.clear()
                    }
                }
            })
            it.duration = 800
            it
        }
        return animation
    }

    private fun setColorButtons() {
        val colors: SharedPreferences
        if (isDarkTheme()) {
            colors = activity?.getSharedPreferences("darkColors", Context.MODE_PRIVATE)!!
            colorButton1 = colors.getString("colorButton1", "#DB070A")!!
            colorButton2 = colors.getString("colorButton2", "#C14B0B")!!
            colorButton3 = colors.getString("colorButton3", "#C206B9")!!
            colorButton4 = colors.getString("colorButton4", "#E1A008")!!
            colorButton5 = colors.getString("colorButton5", "#669443")!!
            colorButton6 = colors.getString("colorButton6", "#4A00F8")!!
            colorButton7 = colors.getString("colorButton7", "#66433D")!!
            colorButton8 = colors.getString("colorButton8", "#1C5872")!!
        } else {
            colors = activity?.getSharedPreferences("lightColors", Context.MODE_PRIVATE)!!
            colorButton1 = colors.getString("colorButton1", "#F94144")!!
            colorButton2 = colors.getString("colorButton2", "#F3722C")!!
            colorButton3 = colors.getString("colorButton3", "#F820ED")!!
            colorButton4 = colors.getString("colorButton4", "#F9C74F")!!
            colorButton5 = colors.getString("colorButton5", "#90BE6D")!!
            colorButton6 = colors.getString("colorButton6", "#8D5BFF")!!
            colorButton7 = colors.getString("colorButton7", "#8F5E56")!!
            colorButton8 = colors.getString("colorButton8", "#277DA1")!!
        }
        binding.mcvButton1.setCardBackgroundColor(Color.parseColor(colorButton1))
        binding.mcvButton2.setCardBackgroundColor(Color.parseColor(colorButton2))
        binding.mcvButton3.setCardBackgroundColor(Color.parseColor(colorButton3))
        binding.mcvButton4.setCardBackgroundColor(Color.parseColor(colorButton4))
        binding.mcvButton5.setCardBackgroundColor(Color.parseColor(colorButton5))
        binding.mcvButton6.setCardBackgroundColor(Color.parseColor(colorButton6))
        binding.mcvButton7.setCardBackgroundColor(Color.parseColor(colorButton7))
        binding.mcvButton8.setCardBackgroundColor(Color.parseColor(colorButton8))
    }

    private fun isDarkTheme(): Boolean =
        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
}