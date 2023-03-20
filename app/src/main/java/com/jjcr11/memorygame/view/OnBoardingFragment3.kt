package com.jjcr11.memorygame.view

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.databinding.DialogCustomBinding
import com.jjcr11.memorygame.databinding.FragmentOnBoarding3Binding
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class OnBoardingFragment3 : Fragment() {

    private lateinit var binding: FragmentOnBoarding3Binding
    private lateinit var colorButton1: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoarding3Binding.inflate(inflater, container, false)

        val bindingDialog: DialogCustomBinding =
            DialogCustomBinding.inflate(layoutInflater)

        bindingDialog.tvLine1.text = "Press the section to"
        bindingDialog.tvLine2.text = "change the color button"

        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .setView(bindingDialog.root)
            .setPositiveButton("Accept") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()

        val colors: SharedPreferences
        if (isDarkTheme()) {
            colors = activity?.getSharedPreferences("darkColors", Context.MODE_PRIVATE)!!
            colorButton1 = colors.getString("colorButton1", "#DB070A")!!
        } else {
            colors = activity?.getSharedPreferences("lightColors", Context.MODE_PRIVATE)!!
            colorButton1 = colors.getString("colorButton1", "#F94144")!!
        }
        binding.mcvButton1.setCardBackgroundColor(Color.parseColor(colorButton1))
        binding.tvColorButton1.text = colorButton1

        val navController = findNavController()

        binding.mcvBack.setOnClickListener {
            navController.popBackStack()
        }

        binding.mcvNext.setOnClickListener {

            navController.navigate(R.id.action_onBoardingFragment3_to_onBoardingFragment4)
        }

        binding.clButton1.setOnClickListener {
            changeColorButton(
                binding.mcvButton1,
                binding.tvColorButton1
            )
        }

        return binding.root
    }

    private fun changeColorButton(
        materialCardView: MaterialCardView,
        textView: TextView,
    ) {
        val builder = ColorPickerDialog.Builder(requireContext())
            .setTitle("Select a color")
            .setPositiveButton("Select", ColorEnvelopeListener { envelope, _ ->
                val color = "#${envelope.hexCode.substring(2..7)}"
                textView.text = color
                materialCardView.setCardBackgroundColor(envelope.color)
                binding.mcvNext.visibility = View.VISIBLE
            })
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .attachAlphaSlideBar(false)

        val colorPickerView = builder.colorPickerView
        val bubbleFlag = BubbleFlag(requireContext())
        val color = materialCardView.cardBackgroundColor.defaultColor

        bubbleFlag.flagMode = FlagMode.ALWAYS
        colorPickerView.flagView = bubbleFlag
        colorPickerView.setInitialColor(color)

        builder.show()
    }

    private fun isDarkTheme(): Boolean =
        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }

}