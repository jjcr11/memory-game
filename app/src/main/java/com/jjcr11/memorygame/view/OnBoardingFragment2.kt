package com.jjcr11.memorygame.view

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.DialogCustomBinding
import com.jjcr11.memorygame.databinding.FragmentOnBoarding2Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.hypot

class OnBoardingFragment2 : Fragment() {

    private lateinit var binding: FragmentOnBoarding2Binding
    private var gameColors = mutableListOf<String>()

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

        val navController = findNavController()

        binding.mcvBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.mcvNext.setOnClickListener {
            navController.navigate(R.id.action_onBoardingFragment2_to_onBoardingFragment3)
        }

        makeDialog("Memorize the sequence", "of 3 colors") {
            startGame()
        }.show()

        setColorButtons()
        disabledAll()

        binding.mcvButton1.setOnClickListener { changeColor(colorButton1) }
        binding.mcvButton2.setOnClickListener { changeColor(colorButton2) }
        binding.mcvButton3.setOnClickListener { changeColor(colorButton3) }
        binding.mcvButton4.setOnClickListener { changeColor(colorButton4) }
        binding.mcvButton5.setOnClickListener { changeColor(colorButton5) }
        binding.mcvButton6.setOnClickListener { changeColor(colorButton6) }
        binding.mcvButton7.setOnClickListener { changeColor(colorButton7) }
        binding.mcvButton8.setOnClickListener { changeColor(colorButton8) }

        return binding.root
    }

    private fun makeDialog(
        line1: String,
        line2: String,
        function: (() -> Unit)?
    ): MaterialAlertDialogBuilder {
        val bindingDialog: DialogCustomBinding =
            DialogCustomBinding.inflate(layoutInflater)

        bindingDialog.tvLine1.text = line1
        bindingDialog.tvLine2.text = line2

        return MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .setView(bindingDialog.root)
            .setPositiveButton("Accept") { dialogInterface, _ ->
                dialogInterface.dismiss()
                function?.invoke()
            }
    }

    private fun startGame() {
        gameColors.clear()
        gameColors.add(colorButton1)
        gameColors.add(colorButton5)
        gameColors.add(colorButton6)

        lifecycleScope.launch(Dispatchers.Main) {
            binding.mcvMain.visibility = View.INVISIBLE
            binding.mcvMain.setBackgroundColor(Color.parseColor(colorButton1))
            startAnimation()
            binding.mcvMain.visibility = View.VISIBLE
            delay(1200)

            binding.mcvMain.visibility = View.INVISIBLE
            binding.mcvMain.setBackgroundColor(Color.parseColor(colorButton5))
            startAnimation()
            binding.mcvMain.visibility = View.VISIBLE
            delay(1200)
            binding.mcvMain.visibility = View.INVISIBLE

            binding.mcvMain.visibility = View.INVISIBLE
            binding.mcvMain.setBackgroundColor(Color.parseColor(colorButton6))
            startAnimation()
            binding.mcvMain.visibility = View.VISIBLE
            delay(1200)
            binding.mcvMain.visibility = View.INVISIBLE

            makeDialog("Press the colors", "in correct order", null).show()

            enabledAll()
        }
    }

    private fun startAnimation() {
        val coords = Pair(binding.mcvMain.width / 2, binding.mcvMain.height / 2)
        val radius = hypot(coords.first.toDouble(), coords.second.toDouble()).toFloat()
        val animation = ViewAnimationUtils.createCircularReveal(
            binding.mcvMain,
            coords.first,
            coords.second,
            0f,
            radius
        )
        animation.duration = 700
        animation.start()
    }

    private fun changeColor(color: String) {
        binding.mcvMain.setBackgroundColor(Color.parseColor(color))
        startAnimation()
        binding.mcvMain.visibility = View.VISIBLE

        if (color == gameColors[0]) {
            gameColors.remove(color)
            if (gameColors.isEmpty()) {
                binding.mcvNext.visibility = View.VISIBLE
            }
        } else {
            makeDialog("You Failed", "Try again") { startGame() }.show()
        }
    }

    private fun disabledAll() {
        binding.mcvButton1.isEnabled = false
        binding.mcvButton2.isEnabled = false
        binding.mcvButton3.isEnabled = false
        binding.mcvButton4.isEnabled = false
        binding.mcvButton5.isEnabled = false
        binding.mcvButton6.isEnabled = false
        binding.mcvButton7.isEnabled = false
        binding.mcvButton8.isEnabled = false
    }

    private fun enabledAll() {
        binding.mcvButton1.isEnabled = true
        binding.mcvButton2.isEnabled = true
        binding.mcvButton3.isEnabled = true
        binding.mcvButton4.isEnabled = true
        binding.mcvButton5.isEnabled = true
        binding.mcvButton6.isEnabled = true
        binding.mcvButton7.isEnabled = true
        binding.mcvButton8.isEnabled = true
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